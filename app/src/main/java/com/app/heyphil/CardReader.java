package com.app.heyphil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class CardReader extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;


    // AID for our loyalty card service.
    private static final String SAMPLE_LOYALTY_CARD_AID = "F222222222";
    private static final String SELECT_APDU_HEADER = "00A40400";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    public static final String accountNo = "102002633993";

    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;

    IntentFilter[] mWriteTagFilters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);

        mTextView = (TextView) findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        } else {
        }



        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            //nfc not support your device.
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        handleIntent(getIntent());

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
    }


    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.activity_pincode, null))
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */

        mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
         setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        //if (mAdapter != null) {
        //    mAdapter.disableForegroundDispatch(this);
        //}
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */

        String action = intent.getAction();
        String type = intent.getType();
        System.out.println("INTENT FOR ==== "+intent);
        //getTagInfo(intent);

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        System.out.println("RAW MESSAGE >>>>> "+rawMsgs);
        NdefMessage[] messages;
        if (rawMsgs != null) {
            messages = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
                // To get a NdefRecord and its different properties from a NdefMesssage
                NdefRecord record = messages[i].getRecords()[i];
                byte[] id = record.getId();
                short tnf = record.getTnf();
                byte[] types = record.getType();
                String message = getTextData(record.getPayload());
                System.out.println("MESSAGE >>>>> "+message);
            }
        }
        handleIntent(intent);
    }



    private void getTagInfo(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String action = getIntent().getAction();


        new NdefReaderTask().execute(tag);
        System.out.println("OUT === "+tag);
    }
    private void handleIntent(Intent intent) {
        String action = intent.getAction();

        System.out.println("INTENT ==== "+action);
        System.out.println("INTENT EQUALS ==== "+NfcAdapter.ACTION_NDEF_DISCOVERED);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            System.out.println("TYPE is ==== "+ type);
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        }else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            //call Unionbank API

            onCreateDialog();
            /*
            OkHttpClient client = new OkHttpClient();
            try{
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"channel_id\":\"BLUEMIX\",\"transaction_id\":\"HC000001\",\"source_account\":\"000000020000\",\"source_currency\":\"php\",\"biller_id\":\"TELCO\",\"reference1\":\"000000000A\",\"reference2\":\"000000000B\",\"reference3\":\"000000000C\",\"amount\":1500.5}");
                Request request = new Request.Builder()
                        .url("https://api.us.apiconnect.ibmcloud.com/ubpapi-dev/sb/api/RESTs/payment")
                        .post(body)
                        .addHeader("x-ibm-client-id", "0a5e08f0-2d33-4b55-a975-cef4dca393c2")
                        .addHeader("x-ibm-client-secret", "dO4wC4eL4fK5oL1nP6gL7xI4vI2tW8bW7wP5sP0gS0oB1sI7dU")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                System.out.println("RESPONSE FROM UNIONBANK "+response);
            }catch (IOException e){
                Toast.makeText(this, "Something happened in processing your payment.", Toast.LENGTH_LONG).show();
            }*/


        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    private String getTextData(byte[] payload) {
        if(payload == null)
            return null;
        try {
            String encoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
            int langageCodeLength = payload[0] & 0077;
            return new String(payload, langageCodeLength + 1, payload.length - langageCodeLength - 1, encoding);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        //adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);


    }

    /**
     * @param activity The corresponding {@link } requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
            IsoDep isoDep = IsoDep.get(tag);
            String action = getIntent().getAction();
            System.out.println("ACTION ==="+action);

                Tag tagFromIntent = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                System.out.println("TAG IS ===="+tagFromIntent);
                Log.i(TAG, Arrays.toString(tagFromIntent.getTechList()));


                try
                {
                    isoDep.connect();

                    byte[] SELECT = {
                            (byte) 0x00, // CLA = 00 (first interindustry command set)
                            (byte) 0xA4, // INS = A4 (SELECT)
                            (byte) 0x04, // P1  = 04 (select file by DF name)
                            (byte) 0x0C, // P2  = 0C (first or only file; no FCI)
                            (byte) 0x06, // Lc  = 6  (data/AID has 6 bytes)
                            (byte) 0x31, (byte) 0x35,(byte) 0x38,(byte) 0x34,(byte) 0x35,(byte) 0x46 // AID = 15845F
                    };

                    byte[] result = isoDep.transceive(SELECT);
                    Log.i(TAG, "SELECT: " + result);

                    if (!(result[0] == (byte) 0x90 && result[1] == (byte) 0x00))
                        throw new IOException("could not select application");

                    byte[] GET_STRING = {
                            (byte) 0x00, // CLA Class
                            (byte) 0xB0, // INS Instruction
                            (byte) 0x00, // P1  Parameter 1
                            (byte) 0x00, // P2  Parameter 2
                            (byte) 0x04  // LE  maximal number of bytes expected in result
                    };

                    result = isoDep.transceive(GET_STRING);
                    Log.i(TAG, "GET_STRING: " + ByteArrayToHexString(result));
                }catch (Exception e) {
                    String error = e.getMessage();
                }

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                System.out.println("NOT SUPPORTED");
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }


        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                mTextView.setText("Read content: " + result);
            }
        }
    }

    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}