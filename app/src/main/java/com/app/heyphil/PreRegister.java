package com.app.heyphil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class PreRegister extends Activity {
    TextView tv_message;
    EditText et_cert;
    EditText et_birthday;
    Button  btn_verify;
    private Context context=this;
    JSONArray jsonArray = null;
    String certno, bday;
    String duplicate;
    String fname;
    String lname;
    String mismatch;
    String success;
    String deactivated;
    String terminated;
    private Calendar myCalendar = Calendar.getInstance();
    private AlertDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_register);
        progressDialog = new SpotsDialog(this, R.style.Custom);
        tv_message=(TextView)findViewById(R.id.message);
        et_cert=(EditText)findViewById(R.id.cert);
        et_birthday=(EditText)findViewById(R.id.birthday);
        btn_verify=(Button)findViewById(R.id.verify);
        et_cert.setAllCaps(true);
        et_birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return false;
            }
        });
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetwork(context))
                {
                    onSubmit();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_birthday.setText(sdf.format(myCalendar.getTime()));
    }
    public static boolean checkNetwork(Context c)
    {
        ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

        return false;
    }
    private void onSubmit(){
        if (!et_cert.getText().toString().trim().isEmpty()&&!et_birthday.getText().toString().trim().isEmpty()){
            certno=et_cert.getText().toString();
            bday=et_birthday.getText().toString();
            new Verify().execute();
        }
        else{
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText("Unable to proceed. Please provide all needed information.");
        }
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class Verify extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Data.DEFAULT_LINK+"/Search.svc/PhilcareWatsonMemberVerification/?certNo="+certno+"&birthDate="+bday, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("PhilcareWatsonMemberVerificationResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        deactivated=c.getString("DeactivatedFlag");
                        duplicate=c.getString("DuplicateFlag");
                        fname=c.getString("FirstName");
                        lname=c.getString("LastName");
                        mismatch=c.getString("MismatchFlag");
                        success=c.getString("SuccessFlag");
                        terminated=c.getString("TerminatedFlag");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(duplicate.equals("1")){
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("You have already an existing account.");
            }
            else if(mismatch.equals("1")){
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("Your Certificate Number and Birthday doesn't match!");
            }
            else if(deactivated.equals("1")){
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("Your account is deactivated.");
            }
            else if(terminated.equals("1")){
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("You can't able to register because your account has been terminated.");
            }
            else if(duplicate.equals("0")&&mismatch.equals("0")&&deactivated.equals("0")&&terminated.equals("0")&&success.equals("0")) {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("Please verify your detail. You may call our customer care hotline at (02) 462-1800.");
            }
            else {
                Intent i=new Intent(PreRegister.this,Registration.class);
                i.putExtra("Cert",certno);
                i.putExtra("Bday",bday);
                i.putExtra("Firstname",fname);
                i.putExtra("Lastname",lname);
                startActivity(i);
                finish();
            }
        }
    }
    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            EditText et_birthday=(EditText)getActivity().findViewById(R.id.birthday);
            et_birthday.setText(year + "-" + (month+1) + "-" +day );

        }
    }
}
