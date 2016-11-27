package com.app.heyphil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Activity {
    private Context context=this;
    RelativeLayout rl_info;
    RelativeLayout rl_success;
    Button facebook;
    TextView tv_skip,tv_message, tv_returnmessage;
    EditText et_firstname,et_lastname,et_mi,et_bldg_no,et_street, et_brgy, et_city, et_province,et_homecontact,et_email,et_contact,et_password,et_confirmpassword;
    Button btn_submit, btn_cancel;
    private CallbackManager callbackManager;
    ProgressDialog progress;
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    JSONArray jsonArray = null;
    String registrationUrl;
    String Certno, bday;
    String duplicate;
    String success;
    static ProgressDialog  	pDialog;
    ArrayList<HashMap<String, String>> locationlist;
    ListAdapter LocationAdapter;
    boolean locstat;
    String locationlink;
    String citylink;
    String loc;
    List<String>            list_xml_link           = new ArrayList<String>();
    List<String>            list_xml_data          = new ArrayList<String>();
    private String          successful_flag         = "";
    private int             maxBarValue2;
    private int             total2;
    private int             currentDownload         = 1;
    ArrayList<HashMap<String, String>> citylist;
    ListAdapter CityAdapter;
    boolean citystat;
    String city;
    List<String>            list_xml_link1           = new ArrayList<String>();
    List<String>            list_xml_data1          = new ArrayList<String>();
    private String          successful_flag1         = "";
    private int             maxBarValue;
    private int             total;
    private int             currentDownload1         = 1;
    boolean 				fbstat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bundle extra=getIntent().getExtras();
        locationlist = new ArrayList<HashMap<String, String>>();
        citylist = new ArrayList<HashMap<String, String>>();
        rl_info=(RelativeLayout)findViewById(R.id.info);
        rl_success=(RelativeLayout)findViewById(R.id.success);
        tv_message=(TextView)findViewById(R.id.tv_message);
        tv_returnmessage=(TextView)findViewById(R.id.returnmessage);
        et_firstname=(EditText)findViewById(R.id.et_firstname);
        et_lastname=(EditText)findViewById(R.id.et_lastname);
        et_mi=(EditText)findViewById(R.id.et_mi);
        et_bldg_no=(EditText)findViewById(R.id.et_bldg_no);
        et_street=(EditText)findViewById(R.id.et_street);
        et_brgy=(EditText)findViewById(R.id.et_brgy);
        et_city=(EditText)findViewById(R.id.et_city);
        et_province=(EditText)findViewById(R.id.et_province);
        et_homecontact=(EditText)findViewById(R.id.et_homecontact);
        et_email=(EditText)findViewById(R.id.et_email);
        et_contact=(EditText)findViewById(R.id.et_contact);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirmpassword=(EditText)findViewById(R.id.et_confirm_password);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        et_firstname.setText(extra.getString("Firstname"));
        et_lastname.setText(extra.getString("Lastname"));
        Certno=extra.getString("Cert");
        bday=extra.getString("Bday");
        locationlink="https://apps.philcare.com.ph/iPhilCare_Mobile/Providers.svc/GetProvidersCity/?";
        new GetProvince().execute();
        et_province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locstat=true;
                ShowLocation();
            }
        });
        et_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_province.getText().toString().trim().isEmpty())
                {
                    ShowCity();
                }
                else
                {
                    et_province.setError("Select Province");
                    return;
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
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
               /* facebook.setVisibility(View.GONE);
                tv_skip.setVisibility(View.GONE);
                rl_info.setVisibility(View.GONE);
                rl_success.setVisibility(View.VISIBLE);
                tv_message.setText("Thank you for registering! An email confirmation has been automatically sent to the email you have provided.");*/
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        /*registrationUrl="https://apps.philcare.com.ph/PhilcareWatson/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=0&email="+et_email.getText().toString()+"&certNo="+Certno+"&userID=&profilePicURL=&mobileNo="+et_contact.getText().toString()+"&userName="+et_email.getText().toString()+"&password="+et_password.getText().toString()+"&bldgNo="+et_bldg_no.getText().toString().replaceAll(" ","+")+"&street="+et_street.getText().toString().replaceAll(" ","+")+"&brgy="+et_brgy.getText().toString().replaceAll(" ","+")+"&city="+et_city.getText().toString().replaceAll(" ","+")+"&province="+et_province.getText().toString().replaceAll(" ","+")+"&homeNo="+et_homecontact.getText().toString().replaceAll(" ","+")+"&birthDate="+bday+"&firstName="+et_firstname.getText().toString().replaceAll(" ","+")+"&lastName="+et_lastname.getText().toString();
        System.out.println("URL"+registrationUrl);
        new Register().execute();*/
        //if(TextUtils.isEmpty(et_brgy.getText().toString())) {et_brgy.setError("Cannot be empty!");return;}
        if(!et_brgy.getText().toString().trim().isEmpty()&&
                !et_city.getText().toString().trim().isEmpty()&&!et_province.getText().toString().trim().isEmpty()&&!et_homecontact.getText().toString().trim().isEmpty()&&
                !et_contact.getText().toString().trim().isEmpty()&&!et_email.getText().toString().trim().isEmpty()&&!et_password.getText().toString().trim().isEmpty()&&!et_confirmpassword.getText().toString().trim().isEmpty()){
            tv_returnmessage.setVisibility(View.GONE);
            if (verifyEmailFormat(et_email))
            {
                if (checkStringLength(et_password.getText().toString(), 6))
                {
                    if (checkIfPasswordIsAlphaNumeric(et_password.getText().toString()))
                    {

                        if (et_password.getText().toString().equals(et_confirmpassword.getText().toString())) {
                            registrationUrl = Data.DEFAULT_LINK+"/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=0&email=" + et_email.getText().toString() + "&certNo=" + Certno + "&userID=&profilePicURL=&mobileNo=" + et_contact.getText().toString() + "&userName=" + et_email.getText().toString() + "&password=" + et_password.getText().toString() + "&bldgNo=" + et_bldg_no.getText().toString().replaceAll(" ", "+") + "&street=" + et_street.getText().toString().replaceAll(" ", "+") + "&brgy=" + et_brgy.getText().toString().replaceAll(" ", "+") + "&city=" + et_city.getText().toString().replaceAll(" ", "+") + "&province=" + et_province.getText().toString().replaceAll(" ", "+") + "&homeNo=" + et_homecontact.getText().toString().replaceAll(" ", "+") + "&birthDate=" + bday + "&firstName=" + et_firstname.getText().toString().replaceAll(" ", "+") + "&lastName=" + et_lastname.getText().toString();
                            System.out.println("URL" + registrationUrl);
                            btn_submit.setEnabled(false);
                            new Register().execute();

                        } else {
                            tv_returnmessage.setVisibility(View.VISIBLE);
                            tv_returnmessage.setText("Password and Confirm Password doesn't match!");
                        }
                    }
                    else
                    {
                        et_password.setError("Your password must contain alphanumeric!");
                        return;
                    }
                }
                else
                {
                    et_password.setError("Minimum of 6 alphanumeric password!");
                    return;
                }
            }
            else
            {

                et_email.setError("Invalid Email!");
                return;
            }
        }
        else
        {
            tv_returnmessage.setVisibility(View.VISIBLE);
            tv_returnmessage.setText("Unable to proceed. Please provide all needed information.");
        }
    }
    public static boolean verifyEmailFormat(EditText et)
    {
        return isValidEmail(et.getText().toString());
    }


    private static boolean isValidEmail(CharSequence target)
    {
        if (target == null) return false;
        else return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean checkStringLength(String s, int i)
    {
        if (s.length() < i) { return false; }
        return true;
    }
    public static boolean checkIfPasswordIsAlphaNumeric(String pwd)
    {
        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).{2,40})");
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }
    private void logoutFromFacebook()
    {
        try {
            if (AccessToken.getCurrentAccessToken() == null) {
                return; // already logged out
            }
            long fb_id=Long.parseLong(facebook_id); //get fb id from sharedprefrences
            GraphRequest graphRequest=new GraphRequest(AccessToken.getCurrentAccessToken(), "/ "+fb_id+"/permissions/", null,
                    HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                }
            });

            graphRequest.executeAsync();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
     btn_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i=new Intent(getBaseContext(),LoginScreen.class);
             startActivity(i);
             finish();
         }
     });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class Register extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registration.this);
            pDialog.setMessage("Please wait...");
            pDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(registrationUrl,ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("PhilcareWatsonMemberRegistrationResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        duplicate=c.getString("DuplicateFlag");
                        success=c.getString("SuccessFlag");
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
            pDialog.dismiss();
            if(success.equals("1")){
                facebook.setVisibility(View.GONE);
                tv_skip.setVisibility(View.GONE);
                rl_info.setVisibility(View.GONE);
                rl_success.setVisibility(View.VISIBLE);
                tv_returnmessage.setVisibility(View.GONE);
                if(fbstat)
                {
                    fbstat=false;
                    tv_message.setText("Thank you for registering! You may now login your account through Facebook and if you don't want to use your Facebook when you login, please set up first your username and password.");
                }
                else
                {
                    tv_message.setText("Thank you for registering! An email confirmation has been automatically sent to the email you have provided.");

                }
            }
            else if(duplicate.equals("1"))
            {
                btn_submit.setEnabled(true);
                rl_info.setVisibility(View.VISIBLE);
                tv_returnmessage.setVisibility(View.VISIBLE);
                tv_returnmessage.setText("Email you have used is already exist!");
            }

        }
    }
    private void getXMLLocation()
    {
        list_xml_link.clear();
        System.out.println("==========url"+convertToUrlLocation(locationlink));
        list_xml_link.add(convertToUrlLocation(locationlink));
    }
    public static String convertToUrlLocation(String urlStr)
    {
        try
        {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            return String.valueOf(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    private class GetProvince extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            getXMLLocation();
        }


        protected String doInBackground(String... params)
        {

            String status = null;

            try
            {
                status =getXMLLoc(list_xml_link.get(0));

                if (status.equals("error"))
                {
                    status = "error";
                }
                else
                {
                    status = getXMLValueLocation(status);
                    total2++;
                }
            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return status;
        }


        protected void onPostExecute(String result)
        {
            if (result.equals("False"))
            {
            }
            else if (result.equals("True"))
            {
                //getXMLLinks();
                new onGetAllXMLDataLocation().execute();
            }
            else if (result.equals("error"))
            {
                successful_flag = "False";

            }
        }
    }
	 /* *//**//**//**//**
     * Get XML data.
     *
     * @param// sevice
     *              - URL on string format
     * @return String - return XML value
     *
     * @response String - returned if error occur
     *//**//**//**//**/
    public static String getXMLLoc(String service)
    {

        String result = null;
        HttpGet request = new HttpGet(service);

        HttpParams httpParams = new BasicHttpParams();
        int some_reasonable_timeout = (int) (50 * 1000);
        HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);
        HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout);
        HttpClient client = new DefaultHttpClient(httpParams);

        try
        {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
            {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                result = responseHandler.handleResponse(response);
            }
            else
            {
                Log.e("trace", "Error1");
                return "error";
            }
        }
        catch (ClientProtocolException e)
        {
            Log.e("trace", "Error2", e);
            return "error";
        }
        catch (IOException e)
        {
            Log.e("trace", "Error3", e);
            return "error";
        }
        finally
        {
            Log.e("trace", "finally");
            client.getConnectionManager().shutdown();
        }

        return result;
    }
    // get All XML Data
    private class onGetAllXMLDataLocation extends AsyncTask<String, String, String>
    {
        int list_xml_link_total;


        protected void onPreExecute()
        {
            maxBarValue2 = list_xml_link.size();
        }


        protected String doInBackground(String... params)
        {
            list_xml_link_total = list_xml_link.size();
            String s;
            for (int i = currentDownload; i < list_xml_link_total; i++)
            {
                s = getXMLLoc(list_xml_link.get(i));
                if (s.equals("error"))
                {
                    currentDownload = i;
                    return "false";
                }
                else
                {
                    list_xml_data.add(s);
                    total2++;

                }

            }

            return "true";
        }


        protected void onPostExecute(String result)
        {
            if (result.equals("true"))
            {
            }
            else
            {
                successful_flag = "False";

            }
        }
    }
    private String getXMLValueLocation(String xml) throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(xml));
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xpp.getName().equals("City") || xpp.getName().equals("a:City"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) loc = xpp.getText();
                    System.out.println(loc);
                    HashMap<String, String> location= new HashMap<String, String>();
                    location.put("Location", loc);
                    locationlist.add(location);
                }
            }
            eventType = xpp.next();
        }
       // System.out.println(locationlist.toString());
        return successful_flag;

    }
    public void ShowLocation() {
        //Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_location=(TextView)dialog.findViewById(R.id.tv_location);
        ListView lv_location=(ListView)dialog.findViewById(R.id.lv_location);
        //tv_location.setTypeface(tf);
        if(locstat){
            tv_location.setText("Select Province");
        }
        if(citystat)
        {
            tv_location.setText("Select City");
        }
        LocationAdapter = new SimpleAdapter(
                Registration.this, locationlist,
                R.layout.single_location, new String[] {"Location"}, new int[] {R.id.tv_location});
        lv_location.setAdapter(LocationAdapter);
        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String province=((TextView) view.findViewById(R.id.tv_location)).getText().toString();
                et_province.setError(null);
                if(!et_province.getText().toString().toString().equals(province))
                {
                    citylist.clear();
                    et_province.setText(province);
                    et_city.setText("");
                    citylink="https://apps.philcare.com.ph/iPhilCare_Mobile/Providers.svc/GetProvidersDistrict/?City="+province.replaceAll(" ","+");
                    dialog.dismiss();
                    new GetCity().execute();

                }
                else
                {
                    citylist.clear();
                    et_province.setText(province);
                    citylink="https://apps.philcare.com.ph/iPhilCare_Mobile/Providers.svc/GetProvidersDistrict/?City="+province.replaceAll(" ","+");
                    dialog.dismiss();
                    new GetCity().execute();
                }


            }
        });
        dialog.show();

    }
    private void getXMLCity()
    {
        list_xml_link1.clear();
        System.out.println("==========url"+convertToUrlCity(citylink));
        list_xml_link1.add(convertToUrlCity(citylink));
    }
    public static String convertToUrlCity(String urlStr)
    {
        try
        {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            return String.valueOf(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    private class GetCity extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            getXMLCity();
        }


        protected String doInBackground(String... params)
        {

            String status = null;

            try
            {
                status =getXMLCit(list_xml_link1.get(0));

                if (status.equals("error"))
                {
                    status = "error";
                }
                else
                {
                    status = getXMLValueCity(status);
                    total++;
                }
            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return status;
        }


        protected void onPostExecute(String result)
        {
            if (result.equals("False"))
            {
            }
            else if (result.equals("True"))
            {
                //getXMLLinks();
                new onGetAllXMLDataCity().execute();
            }
            else if (result.equals("error"))
            {
                successful_flag1 = "False";

            }
        }
    }
	 /* *//**//**//**//**
     * Get XML data.
     *
     * @param// sevice
     *              - URL on string format
     * @return String - return XML value
     *
     * @response String - returned if error occur
     *//**//**//**//**/
    public static String getXMLCit(String service)
    {

        String result = null;
        HttpGet request = new HttpGet(service);

        HttpParams httpParams = new BasicHttpParams();
        int some_reasonable_timeout = (int) (50 * 1000);
        HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);
        HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout);
        HttpClient client = new DefaultHttpClient(httpParams);

        try
        {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
            {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                result = responseHandler.handleResponse(response);
            }
            else
            {
                Log.e("trace", "Error1");
                return "error";
            }
        }
        catch (ClientProtocolException e)
        {
            Log.e("trace", "Error2", e);
            return "error";
        }
        catch (IOException e)
        {
            Log.e("trace", "Error3", e);
            return "error";
        }
        finally
        {
            Log.e("trace", "finally");
            client.getConnectionManager().shutdown();
        }

        return result;
    }
    // get All XML Data
    private class onGetAllXMLDataCity extends AsyncTask<String, String, String>
    {
        int list_xml_link_total;


        protected void onPreExecute()
        {
            maxBarValue = list_xml_link1.size();
        }


        protected String doInBackground(String... params)
        {
            list_xml_link_total = list_xml_link1.size();
            String s;
            for (int i = currentDownload1; i < list_xml_link_total; i++)
            {
                s = getXMLCit(list_xml_link1.get(i));
                if (s.equals("error"))
                {
                    currentDownload1 = i;
                    return "false";
                }
                else
                {
                    list_xml_data1.add(s);
                    total++;

                }

            }

            return "true";
        }


        protected void onPostExecute(String result)
        {
            if (result.equals("true"))
            {
            }
            else
            {
                successful_flag1 = "False";

            }
        }
    }
    private String getXMLValueCity(String xml) throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(xml));
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xpp.getName().equals("District") || xpp.getName().equals("a:District"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) city = xpp.getText();
                    System.out.println(city);
                    HashMap<String, String> district= new HashMap<String, String>();
                    district.put("Location", city);
                    citylist.add(district);

                }
            }
            eventType = xpp.next();
        }
        // System.out.println(locationlist.toString());
        return successful_flag1;

    }
    public void ShowCity() {
        //Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_location=(TextView)dialog.findViewById(R.id.tv_location);
        ListView lv_location=(ListView)dialog.findViewById(R.id.lv_location);
        //tv_location.setTypeface(tf);
        tv_location.setText("Select City");
        CityAdapter = new SimpleAdapter(
                Registration.this, citylist,
                R.layout.single_location, new String[] {"Location"}, new int[] {R.id.tv_location});
        lv_location.setAdapter(CityAdapter);
        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String district=((TextView) view.findViewById(R.id.tv_location)).getText().toString();
                et_city.setText(district);
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,LoginScreen.class);
        startActivity(i);
        finish();
    }
}
