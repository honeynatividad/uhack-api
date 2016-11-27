package com.app.heyphil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class LoginScreen extends Activity {
	
	private EditText	et_username;
	private EditText	et_password;
	
	private Button		btn_login;
	private Button 	facebook;
	private TextView	tv_signup;
	private TextView	tv_forgotpassword;
	
	private String		login_link;
	private String		Link="https://heyphil.mybluemix.net/cert=";
	private String		request_password;
	private String		user;
	private String		pass;
	
	List<String>            list_xml_link           = new ArrayList<String>();
	List<String>            list_xml_data           = new ArrayList<String>();
	private String          successful_flag         = "";
	private String         	text_cert         = "";
	private String          fname         = "";
	private String         	lname         = "";
	private String         	email         = "";
	private String			message;
	private int             maxBarValue;
	private int             total;
	private int             currentDownload         = 1;
	List<String>            list_xml_link2           = new ArrayList<String>();
	List<String>            list_xml_data2           = new ArrayList<String>();
	private String          successful_flag2         = "";
	private int             maxBarValue2;
	private int             total2;
	private int             currentDownload2         = 1;
	List<String>            list_xml_link3           = new ArrayList<String>();
	List<String>            list_xml_data3           = new ArrayList<String>();
	private String          successful_flag3         = "";
	private int             maxBarValue3;
	private int             total3;
	private int             currentDownload3         = 1;
	static ProgressDialog  	pDialog;
	static AlertDialog     aDialog;
	private Context			context=this;
	private Context         context1;
	private static String   CURRENT_SCREEN          = "LoginScreen";
	private String 			last_name;
	private String 			first_name;
	private String 			mi;
	private String 			sex;
	private String 			civil_status;
	private String 			birthday;
	private String 			home_address;
	private String 			home_number;
	private String 			mobile_number;
	private String 			certificate_number;
	private String 			member_type;
	private String 			agreement_name;
	private String 			agreement_no;
	private String 			effectivity_date;
	private String 			expiration_date;
	private String 			bldg_no;
	private String 			street;
	private String 			brgy;
	private String 			city;
	private String 			province;
	private String 			ben_pack;
	private String 			plan_type;
	private String 			room_desc;
	private String 			ben_limit;
	private String 			hospitals;
	private String 			pre_ex;
	private String 			philhealth;
	private String 			riders;
	private String 			room_rate;
	private String 			ape;
	private String 			dental;
	private String 			policyno;
	private CallbackManager callbackManager;
	ProgressDialog 			progress;
	private String 			facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;
	boolean 				fbstat;
	boolean 				fb;
	JSONArray 				jsonArray = null;
	String 					Certno, Bday;
	String 					Duplicate;
	String 					Fname;
	String 					Lname;
	String 					Mismatch;
	String 					Success;
	String 					Deactivated;
	String 					Terminated;
	private Calendar 		myCalendar = Calendar.getInstance();
	private AlertDialog 	progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
		setContentView(R.layout.login_screen);
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;
		//Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		et_username=(EditText)findViewById(R.id.et_username);
		et_password=(EditText)findViewById(R.id.et_password);
		btn_login=(Button)findViewById(R.id.btn_login);
		tv_signup=(TextView)findViewById(R.id.tv_btn_sign_up);
		tv_forgotpassword=(TextView)findViewById(R.id.tv_btn_forgot_password);
		/*tv_signup.setTypeface(tf);
		tv_forgotpassword.setTypeface(tf);
		et_username.setTypeface(tf);
		et_password.setTypeface(tf);*/
		if(Data.logout)
		{
			context.getSharedPreferences("Login",0).edit().clear().commit();
			Data.logout=false;
		}

		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isStoragePermissionGranted();
		}
		catch(Exception ex) {}
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			Data.tts=false;
			if(!Data.android_lower_version)
			{
				Data.android_lower_version=true;
				final Dialog dialog = new Dialog(this);
				dialog.setTitle("Android Version");
				dialog.setContentView(R.layout.instruction);
				dialog.getWindow().setGravity(Gravity.CENTER);
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				TextView instruction=(TextView)dialog.findViewById(R.id.instruction);
				//instruction.setTypeface(tf);
				instruction.setText("Your Andrroid OS version is "+android.os.Build.VERSION.RELEASE+", Heyphil voice requires a minimum Android OS version 5.0!");
				dialog.show();

			}
		}
		else
		{
			Data.tts=true;
		}
			if (!gps_enabled && !network_enabled) {
				// notify user
				Builder dialog = new Builder(context);
				dialog.setMessage("Please enable your GPS! ");
				dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						// TODO Auto-generated method stub
						Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						context.startActivity(myIntent);
						//get gps
					}
				});
				dialog.show();
			} else {
				SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
				final String pass = sp1.getString("Psw", "");
				final String user = sp1.getString("User", "");
				if (pass != null && !pass.isEmpty() && user != null && !user.isEmpty()) {
					btn_login.setEnabled(false);
					et_username.setText(user);
					et_password.setText(pass);
					onLogin();
				} else {
					et_username.setText("");
					et_password.setText("");
				}
			}
		//btn_login.setTypeface(tf);
		btn_login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkNetwork(context)){
					fbstat=false;
					    onLogin();
				}
				else{
					Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
			}});
		tv_forgotpassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				forgotpassword();
			}
		});
		tv_signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getBaseContext(),PreRegister.class);
				startActivity(intent);
				finish();
			}});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	public  boolean isStoragePermissionGranted() {
		Boolean check;
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED ) {

				check = true;
			}else {
				ActivityCompat.requestPermissions(this, new String[]{
						android.Manifest.permission.BLUETOOTH,
						android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
						android.Manifest.permission.ACCESS_COARSE_LOCATION,
						android.Manifest.permission.ACCESS_WIFI_STATE,
				}, 1);

				check = false;
			}

			return check;

		}else { //permission is automatically granted on sdk<23 upon installation
			Log.v("TAG","Permission is granted");
			return true;
		}


	}
	 // check Login input
	  private void onLogin()
	  {
		  if(TextUtils.isEmpty(et_username.getText().toString())) {
			  successful_flag = "False";
			  et_username.setError("Username Required!");
			  return;
		  }
		  else if(TextUtils.isEmpty(et_password.getText().toString())) {
			  successful_flag = "False";
			  et_password.setError("Password Required!");
			  return;
		  }
		  else {
			  new onLoginAsync().execute();
		  }
	  }
	  // get Login Information
	  private class onLoginAsync extends AsyncTask<String, String, String>
	  {
			protected void onPreExecute()
			{
				if(fbstat)
				{
					getLoginXMLfacebook();
				}
				else
				{
					getLoginXML();
				}
				progressDialog = new SpotsDialog(context, R.style.Custom);
				progressDialog.show();
			}


			protected String doInBackground(String... params)
			{

				  String status = null;

				  try
				  {
						status =getXML(list_xml_link.get(0));

						if (status.equals("error"))
						{
							  status = "error";
						}
						else
						{
							  status = getXMLValue(status);
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
					  progressDialog.dismiss();
					  btn_login.setEnabled(true);
					  if(fb&&message.contains("Incorrect"))
					  {
						  fb=false;
							  Builder dialog = new Builder(LoginScreen.this);
							  dialog.setTitle("Login Failed");
							  dialog.setMessage("Your email doesn't exist. To access your account we need your certificate no and birthday to verify.");
							  dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
								  public void onClick(DialogInterface dialog, int which) {
									  verify();
								  }
							  });
							  dialog.show();
					  }
					  else
					  {
						  Builder dialog = new Builder(LoginScreen.this);
						  dialog.setTitle("Login Failed");
						  dialog.setMessage(message);
						  dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							  public void onClick(DialogInterface dialog, int which) {
								  et_username.setText("");
								  et_password.setText("");
								  dialog.dismiss();
							  }
						  });
						  dialog.show();
					  }
				  }
				  else if (result.equals("True"))
				  {
					  	//getXMLLinks();
					  SharedPreferences sp=getSharedPreferences("Login", 0);
					  SharedPreferences.Editor Ed=sp.edit();
					  Ed.putString("User",et_username.getText().toString());
					  Ed.putString("Psw",et_password.getText().toString());
					  Ed.commit();
					  new onGetAllXMLData().execute();
				  }
				  else if (result.equals("error"))
				  {
						successful_flag = "False";
						
				  }
			}
	  }
	// get All XML Data
		  private class onGetAllXMLData extends AsyncTask<String, String, String>
		  {
				int list_xml_link_total;

				protected void onPreExecute()
				{
					  maxBarValue = list_xml_link.size();
				}


				protected String doInBackground(String... params)
				{
					  list_xml_link_total = list_xml_link.size();
					  String s;
					  for (int i = currentDownload; i < list_xml_link_total; i++)
					  {
							s = getXML(list_xml_link.get(i));
							if (s.equals("error"))
							{
								  currentDownload = i;
								  return "false";
							}
							else
							{
								  list_xml_data.add(s);
								  total++;
								 
							}

					  }

					  return "true";
				}


				protected void onPostExecute(String result)
				{
					  if (result.equals("true"))
					  {
							  successful_flag = "True";
							  String url = "http://philcare.com.ph/api/heyphil/api.php/credentials";
							  new loadWatsonCredential().execute(url);
							  new onMemberAsync().execute();
					  }
					  else
					  {
						  successful_flag = "False";
					  }

				}
		  }

	private class loadWatsonCredential extends AsyncTask<String, String, String>
	{
		protected void onPreExecute()
		{

		}


		protected String doInBackground(String... params)
		{
			ServiceHandler sh = new ServiceHandler();
			String Producturl="http://philcare.com.ph/api/heyphil/api.php/credentials";
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Producturl, ServiceHandler.GET);
			Log.d("Response: ", ">>>FROM LOGIN " + jsonStr);

			if (jsonStr != null) {
				JSONObject jobjectness;
				try {
					////whole response
					JSONObject jobbb = new JSONObject(jsonStr);
					//wp_post content
					jobjectness = jobbb.getJSONObject("credentials");
					//records content
					JSONArray jarr2 = jobjectness.getJSONArray("records");
					for (int i = 0; i < jarr2.length(); i++) {
						///reco
						JSONArray jarr3 = new JSONArray(jarr2.get(i).toString());
						if(jarr3.get(3).equals("dialog")) {
							System.out.println("============HEYPHIL DIALOG" + jarr3.getString(1));
							System.out.println("============HEYPHIL DIALOG" + jarr3.getString(2));
							Data.dialogUsername = jarr3.getString(1);
							Data.dialogPassword = jarr3.getString(2);
							Data.dialogDialogId = jarr3.getString(4);
						}else if(jarr3.get(3).equals("nlc")){
							Data.nlcUsername = jarr3.getString(1);
							Data.nlcPassword = jarr3.getString(2);
							Data.nlcClassifierId = jarr3.getString(4);
							System.out.println("CLASSIFIER ID IS "+ Data.nlcClassifierId);
						}else if(jarr3.get(3).equals("tts")){
							Data.ttsUsername = jarr3.getString(1);
							Data.ttsPassword = jarr3.getString(2);
						}
					}
				} catch (JSONException e) {

					e.printStackTrace();

				}
			}
			else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}


		protected void onPostExecute(String stream)
		{
			String customURL = "https://philcare.com.ph/gateway/upload/profile/"+ Data.cert+".jpg";
			MyTask task = new MyTask();
			task.execute(customURL);

		}
	}
	private class MyTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				HttpURLConnection.setFollowRedirects(false);
				HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
				con.setRequestMethod("HEAD");
				System.out.println(con.getResponseCode()== HttpURLConnection.HTTP_OK);
				return (con.getResponseCode()== HttpURLConnection.HTTP_OK);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			boolean bResponse = result;
			if (bResponse == true &&!fbstat)
			{
					//Toast.makeText(LoginScreen.this, "File exists!", Toast.LENGTH_SHORT).show();
					new LoadImage().execute("https://philcare.com.ph/gateway/upload/profile/" + Data.cert + ".jpg");
			}
			else
			{
				//Toast.makeText(LoginScreen.this, "File does not exist!", Toast.LENGTH_SHORT).show();
				if(fbstat)
				{
					fbstat=false;
					System.out.println("False Bitmap Result"+Data.fbprofile);
					new LoadImage().execute(Data.fbprofile);
					System.out.println("False Bitmap Result"+Data.bitmap);
					Data.Bitmap=true;
					if(fb)
					{
						DateFormat inputFormat = new SimpleDateFormat("mm/dd/yyyy");
						DateFormat outputFormat = new SimpleDateFormat("yyyy-mm-dd");
						String inputDateStr=Data.birthday;
						Date date = null;
						try {
							date = inputFormat.parse(inputDateStr);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Bday = outputFormat.format(date);
						System.out.println("DATE"+Bday);
						new Update().execute();
					}
				}
				else
				{
					Data.Bitmap = false;
					//Toast.makeText(LoginScreen.this, "Image Does Not exist or Network Error"+Data.bitmap, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getBaseContext(), ChatBubbleActivity.class);
					startActivity(intent);
					et_username.setText("");
					et_password.setText("");
					finishAffinity();
				}
			}
			progressDialog.dismiss();
		}
	}
	// get username and password
		  private void getLoginXML()
		  {
				user = et_username.getText().toString();
				pass= et_password.getText().toString();
				login_link = "/Login.svc/Login/?Username=" + user + "&pwd=" + pass+"&UserID=&Email=&FbFrom=0";
				login_link = Data.DEFAULT_LINK + login_link;
				list_xml_link.clear();
				System.out.println("==========url"+convertToUrl(login_link));
				list_xml_link.add(convertToUrl(login_link));
		  }
	private void getLoginXMLfacebook()
	{
		user = et_username.getText().toString();
		pass= et_password.getText().toString();
		login_link = "/Login.svc/Login/?Username=&pwd=&UserID="+facebook_id+"&Email="+email_id+"&FbFrom=1";
		login_link = Data.DEFAULT_LINK + login_link;
		list_xml_link.clear();
		System.out.println("==========url"+convertToUrl(login_link));
		list_xml_link.add(convertToUrl(login_link));
	}
		  // get XML value Data
		  private String getXMLValue(String xml) throws XmlPullParserException, IOException
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
							if (xpp.getName().equals("SuccessFlag") || xpp.getName().equals("a:SuccessFlag"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) successful_flag = xpp.getText();
							}
							else if (xpp.getName().equals("CertNo") || xpp.getName().equals("a:CertNo"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) text_cert = xpp.getText();
								  Data.cert=text_cert;
							}
							else if (xpp.getName().equals("Email") || xpp.getName().equals("a:Email"))
							{
								if (xpp.next() == XmlPullParser.TEXT) email = xpp.getText();
								Data.email=email;
							}
							else if (xpp.getName().equals("FirstName") || xpp.getName().equals("a:FirstName"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) fname = xpp.getText();
								  Data.Fname=fname;
							}
							else if (xpp.getName().equals("LastName") || xpp.getName().equals("a:LastName"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) lname = xpp.getText();
								  Data.Lname=lname;
							}
							else if (xpp.getName().equals("MessageReturn") || xpp.getName().equals("a:MessageReturn"))
							{
								if (xpp.next() == XmlPullParser.TEXT) message= xpp.getText();
							}
							else if (xpp.getName().equals("Server") || xpp.getName().equals("a:Server"))
							{
								if (xpp.next() == XmlPullParser.TEXT) Data.server=xpp.getText();
							}
							else if (xpp.getName().equals("UserNameOut") || xpp.getName().equals("a:UserNameOut"))
							{
								if (xpp.next() == XmlPullParser.TEXT) Data.username=xpp.getText().toString();
							}
							else if (xpp.getName().equals("PasswordOut") || xpp.getName().equals("a:PasswordOut"))
							{
								if (xpp.next() == XmlPullParser.TEXT) Data.password=xpp.getText();
							}
					  }
					  eventType = xpp.next();
					  
				}

				return successful_flag;

		  }
		  /**
		   * Get XML data.
		   *
		   * @paramsevice
		   *              - URL on string format
		   * @return String - return XML value
		   *
		   * @response String - returned if error occur
		   */
		  public String getXML(String service)
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
		  public static String convertToUrl(String urlStr)
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
		/**
		   * Progress Dialog/ Loading Dialog
		   *
		   * Method call if you want to display Loading Dialog
		   *
		   * @param c
		   *              - Context
		   * @param msg
		   *              - Message to be display in dialog
		   */
		  public static void progressDialog(Context c, String msg)
		  {
				pDialog = ProgressDialog.show(c, "", msg, true);
				pDialog.show();
		  }
		  /**
		   * Close Dialog
		   *
		   * Method call if you want to close dialog
		   */
		  public static void closeProgressDialog()
		  {
				pDialog.dismiss();
		  }
		 /* public static boolean checkNetwork(Context c)
		  {
				ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

				NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

				return false;
		  }*/
		  
		  //AlertDialog
		  private void alertdialog(){
			  Builder alertDialog2 = new Builder(
					    LoginScreen.this);

					// Setting Dialog Title
					alertDialog2.setTitle("Exit Application");

					// Setting Dialog Message
					alertDialog2.setMessage("Are you sure you want to exit Heyphil?");

					// Setting Icon to Dialog
					alertDialog2.setIcon(R.drawable.appdialog);

					// Setting Positive "Yes" Btn
					alertDialog2.setPositiveButton("YES",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            // Write your code here to execute after dialog
					        	finish();
					        }
					    });

					// Setting Negative "NO" Btn
					alertDialog2.setNegativeButton("NO",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            // Write your code here to execute after dialog
					            dialog.cancel();
					        }
					    });

					// Showing Alert Dialog
					alertDialog2.show();
		  }
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			alertdialog();
		}
	  public static boolean checkNetwork(Context c)
	  {
			ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

			return false;
	  }
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		@Override
		protected void onPreExecute() {
			Data.bitmap=null;
			super.onPreExecute();

		}
		protected Bitmap doInBackground(String... args) {
			try {
				Data.bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
				System.out.println("FBProfile2"+Data.fbprofile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Data.bitmap;
		}

		protected void onPostExecute(Bitmap image) {

			if(image != null){
				Data.Bitmap=true;
				System.out.println("True Bitmap Result"+Data.bitmap);
				//Toast.makeText(LoginScreen.this, "Image", Toast.LENGTH_SHORT).show();
				Intent intent =new Intent(getBaseContext(),ChatBubbleActivity.class);
				startActivity(intent);
				et_username.setText("");
				et_password.setText("");
				finishAffinity();
			}else{
				Data.Bitmap=false;
				System.out.println("False Bitmap Result"+Data.bitmap);
				//Toast.makeText(LoginScreen.this, "Image Does Not exist or Network Error"+Data.bitmap, Toast.LENGTH_SHORT).show();
				Intent intent =new Intent(getBaseContext(),ChatBubbleActivity.class);
				startActivity(intent);
				et_username.setText("");
				et_password.setText("");
				finishAffinity();
			}
			progressDialog.dismiss();
		}
	}
	///=======================Forgot Password======================================
	private void getXMLForgot()
	{
		list_xml_link3.clear();
		System.out.println("==========url"+convertToUrlForgot(request_password));
		list_xml_link3.add(convertToUrlForgot(request_password));
	}
	public static String convertToUrlForgot(String urlStr)
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
	private class onForgotAsync extends AsyncTask<String, String, String>
	{
		protected void onPreExecute()
		{
			getXMLForgot();
			progressDialog(context, "Verifying data. Please wait...");
		}


		protected String doInBackground(String... params)
		{

			String status = null;

			try
			{
				status =getXMLForgotPass(list_xml_link3.get(0));

				if (status.equals("error"))
				{
					status = "error";
				}
				else
				{
					status = getXMLValueForgot(status);
					total3++;
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
				new onGetAllXMLDataForgot().execute();
			}
			else if (result.equals("error"))
			{
				successful_flag3 = "False";

			}
			Builder dialog = new Builder(LoginScreen.this);
			dialog.setTitle("Forgot Password");
			dialog.setMessage(message);
			dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			dialog.show();
			closeProgressDialog();
		}
	}
	// get All XML Data
	private class onGetAllXMLDataForgot extends AsyncTask<String, String, String>
	{
		int list_xml_link_total;


		protected void onPreExecute()
		{
			maxBarValue3 = list_xml_link3.size();
		}


		protected String doInBackground(String... params)
		{
			list_xml_link_total = list_xml_link3.size();
			String s;
			for (int i = currentDownload3; i < list_xml_link_total; i++)
			{
				s = getXMLForgotPass(list_xml_link3.get(i));
				if (s.equals("error"))
				{
					currentDownload3 = i;
					return "false";
				}
				else
				{
					list_xml_data3.add(s);
					total3++;

				}

			}

			return "true";
		}

		protected void onPostExecute(String result)
		{
			if (result.equals("true"))
			{
				successful_flag3 = "True";
			}
			else
			{
				successful_flag3 = "False";
			}

		}
	}
	public String getXMLForgotPass(String service)
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
	private String getXMLValueForgot(String xml) throws XmlPullParserException, IOException
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
				if (xpp.getName().equals("SuccessFlag") || xpp.getName().equals("a:SuccessFlag"))
				{
					if (xpp.next() == XmlPullParser.TEXT) successful_flag = xpp.getText();
				}
				else if (xpp.getName().equals("MessageReturn") || xpp.getName().equals("a:MessageReturn"))
				{
					if (xpp.next() == XmlPullParser.TEXT) message= xpp.getText();
				}
			}
			eventType = xpp.next();

		}

		return successful_flag3;

	}

	public void verify() {
		//Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.verify);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_verify=(TextView)dialog.findViewById(R.id.tv_verify);
		TextView tv_cert=(TextView)dialog.findViewById(R.id.tv_cert);
		TextView tv_bday=(TextView)dialog.findViewById(R.id.tv_bday);
		final EditText et_cert=(EditText)dialog.findViewById(R.id.et_cert);
		final EditText et_bday=(EditText)dialog.findViewById(R.id.et_bday);
		Button btn_submit=(Button)dialog.findViewById(R.id.btn_submit);
		Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
		/*tv_verify.setTypeface(tf);tv_cert.setTypeface(tf);tv_bday.setTypeface(tf);et_cert.setTypeface(tf);et_bday.setTypeface(tf);
		btn_submit.setTypeface(tf);btn_cancel.setTypeface(tf);*/
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				String myFormat = "yyyy-MM-dd"; //In which you need put here
				SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

				et_bday.setText(sdf.format(myCalendar.getTime()));
			}

		};
		et_bday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(LoginScreen.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					Certno=et_cert.getText().toString().trim();
					Bday=et_bday.getText().toString().trim();
					new Verify().execute();
					dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private class Verify extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Data.DEFAULT_LINK+"/Search.svc/PhilcareWatsonMemberVerification/?certNo="+Certno+"&birthDate="+Bday, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("PhilcareWatsonMemberVerificationResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						Deactivated=c.getString("DeactivatedFlag");
						Duplicate=c.getString("DuplicateFlag");
						Fname=c.getString("FirstName");
						Lname=c.getString("LastName");
						Mismatch=c.getString("MismatchFlag");
						Success=c.getString("SuccessFlag");
						Terminated=c.getString("TerminatedFlag");
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
					Builder dialog = new Builder(LoginScreen.this);
					dialog.setTitle("Successfully verified!");
					dialog.setMessage("Login Automatically");
					dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new Update().execute();
						}
					});
					dialog.show();
		}
	}
	private class Update extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Data.DEFAULT_LINK+"/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=1&email="+email_id+"&certNo="+Certno+"&userID="+facebook_id+"&profilePicURL="+Data.fbprofile+"&mobileNo=&userName==&password=&bldgNo=&street=&brgy=&city=&province=&homeNo=&birthDate="+Bday+"&firstName=&lastName=",ServiceHandler.GET);
			System.out.println(Data.DEFAULT_LINK+"/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=1&email="+email_id+"&certNo="+Certno+"&userID="+facebook_id+"&profilePicURL="+Data.fbprofile+"&mobileNo=&userName=&password=&bldgNo=&street=&brgy=&city=&province=&homeNo=&birthDate="+Bday+"&firstName=&lastName=");
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("PhilcareWatsonMemberRegistrationResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						Duplicate=c.getString("DuplicateFlag");
						Success=c.getString("SuccessFlag");
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
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			if(!fb)
			{
				if (Duplicate.equals("1")) {
					fbstat = true;
					new onLoginAsync().execute();
				}
				if (Success.equals("1")) {
					fbstat = true;
					new onLoginAsync().execute();
				}
			}
			else
			{
				fb=false;
			}
		}
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
	}
	public void forgotpassword() {
		//Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(LoginScreen.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.forgot_password);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_forgot = (TextView) dialog.findViewById(R.id.tv_forgotpassword);
		TextView tv_email = (TextView) dialog.findViewById(R.id.tv_email);
		final EditText et_email = (EditText) dialog.findViewById(R.id.et_email);
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_email.getText().toString().trim().isEmpty())
				{
					String email = et_email.getText().toString().trim();
					request_password=Data.DEFAULT_LINK+"/Login.svc/RequestPassword_v2/?Email="+email;
					et_email.setText("");
					dialog.dismiss();
					new onForgotAsync().execute();

				}
				else{
					et_email.setError("Empty field!");
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	private void getXMLMemberinfo()
	{
		String mem_link = Data.DEFAULT_LINK+"/Members.svc/MembersInfo/?CertNo="+ Data.cert;
		list_xml_link2.clear();
		System.out.println("==========url"+convertToUrlMember(mem_link));
		list_xml_link2.add(convertToUrlMember(mem_link));
	}
	public static String convertToUrlMember(String urlStr)
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
	private class onMemberAsync extends AsyncTask<String, String, String>
	{
		protected void onPreExecute()
		{
			getXMLMemberinfo();
		}


		protected String doInBackground(String... params)
		{

			String status = null;

			try
			{
				status =getXMLMember(list_xml_link2.get(0));

				if (status.equals("error"))
				{
					status = "error";
				}
				else
				{
					status = getXMLValueMember(status);
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
				new onGetAllXMLDataMember().execute();
			}
			else if (result.equals("error"))
			{
				successful_flag2 = "False";

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
	public static String getXMLMember(String service)
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
	private class onGetAllXMLDataMember extends AsyncTask<String, String, String>
	{
		int list_xml_link_total;


		protected void onPreExecute()
		{
			maxBarValue2 = list_xml_link2.size();
		}


		protected String doInBackground(String... params)
		{
			list_xml_link_total = list_xml_link2.size();
			String s;
			for (int i = currentDownload2; i < list_xml_link_total; i++)
			{
				s = getXMLMember(list_xml_link2.get(i));
				if (s.equals("error"))
				{
					currentDownload2 = i;
					return "false";
				}
				else
				{
					list_xml_data2.add(s);
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
				successful_flag2 = "False";

			}
		}
	}
	private String getXMLValueMember(String xml) throws XmlPullParserException, IOException
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
				if (xpp.getName().equals("APE") || xpp.getName().equals("a:APE"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ape = xpp.getText();
					Data.ape=ape;
				}
				else if (xpp.getName().equals("Address") || xpp.getName().equals("a:Address"))
				{
					if (xpp.next() == XmlPullParser.TEXT) home_address = xpp.getText();
					Data.home_address = home_address;
				}
				else if (xpp.getName().equals("AgreementName") || xpp.getName().equals("a:AgreementName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) agreement_name = xpp.getText();
					Data.agreement_name=agreement_name;
				}
				else if (xpp.getName().equals("AgreementNo") || xpp.getName().equals("a:AgreementNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) agreement_no = xpp.getText();
					Data.agreement_no=agreement_no;
				}
				else if (xpp.getName().equals("Barangay") || xpp.getName().equals("a:Barangay"))
				{
					if (xpp.next() == XmlPullParser.TEXT) brgy = xpp.getText();
					Data.brgy=" "+brgy;
				}
				else if (xpp.getName().equals("BenefitLimit") || xpp.getName().equals("a:BenefitLimit"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ben_limit = xpp.getText();
					Data.ben_limit=ben_limit;
				}
				else if (xpp.getName().equals("BirthDate") || xpp.getName().equals("a:BirthDate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) birthday = xpp.getText();
					Data.birthday=birthday;
				}
				else if (xpp.getName().equals("CertNo") || xpp.getName().equals("a:CertNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) certificate_number = xpp.getText();
					Data.certificate_number=certificate_number;
					Certno=certificate_number;
				}
				else if (xpp.getName().equals("City") || xpp.getName().equals("a:City"))
				{
					if (xpp.next() == XmlPullParser.TEXT) city = xpp.getText();
					Data.city=" "+city;
				}
				else if (xpp.getName().equals("CivilStat") || xpp.getName().equals("a:CivilStat"))
				{
					if (xpp.next() == XmlPullParser.TEXT) civil_status = xpp.getText();
					Data.civil_status=civil_status;
				}
				else if (xpp.getName().equals("ContactNumber") || xpp.getName().equals("a:ContactNumber"))
				{
					if (xpp.next() == XmlPullParser.TEXT) mobile_number = xpp.getText();
					Data.mobile_number=" "+mobile_number;
				}
				else if (xpp.getName().equals("Dental") || xpp.getName().equals("a:Dental"))
				{
					if (xpp.next() == XmlPullParser.TEXT) dental = xpp.getText();
					Data.dental=dental;
				}
				else if (xpp.getName().equals("EffectiveDate") || xpp.getName().equals("a:EffectiveDate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) effectivity_date = xpp.getText();
					Data.effectivity_date=effectivity_date;
				}
				else if (xpp.getName().equals("ExpiryDate") || xpp.getName().equals("a:ExpiryDate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) expiration_date = xpp.getText();
					Data.expiration_date=expiration_date;
				}
				else if (xpp.getName().equals("FirstName") || xpp.getName().equals("a:FirstName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) first_name = xpp.getText();
					Data.first_name=first_name;
				}
				else if (xpp.getName().equals("HomeNo") || xpp.getName().equals("a:HomeNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) home_number = xpp.getText();
				}
				else if (xpp.getName().equals("Hospitals") || xpp.getName().equals("a:Hospitals"))
				{
					if (xpp.next() == XmlPullParser.TEXT) hospitals = xpp.getText();
					Data.hospitals=hospitals;
				}
				else if (xpp.getName().equals("HouseNo") || xpp.getName().equals("a:HouseNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) bldg_no = xpp.getText();
					Data.bldg_no=" "+bldg_no;
				}
				else if (xpp.getName().equals("LastName") || xpp.getName().equals("a:LastName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) last_name = xpp.getText();
					Data.last_name=last_name;
				}
				else if (xpp.getName().equals("MemberType") || xpp.getName().equals("a:MemberType"))
				{
					if (xpp.next() == XmlPullParser.TEXT) member_type = xpp.getText();
					Data.member_type=member_type;

				}
				else if (xpp.getName().equals("MiddleName") || xpp.getName().equals("a:MiddleName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) mi = xpp.getText();
					Data.mi=mi;
				}
				else if (xpp.getName().equals("PackageDescription") || xpp.getName().equals("a:PackageDescription"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ben_pack = xpp.getText();
					Data.ben_pack=ben_pack;
				}
				else if (xpp.getName().equals("PhilHealth") || xpp.getName().equals("a:PhilHealth"))
				{
					if (xpp.next() == XmlPullParser.TEXT) philhealth = xpp.getText();
					Data.philhealth=philhealth;
				}
				else if (xpp.getName().equals("PlanType") || xpp.getName().equals("a:PlanType"))
				{
					if (xpp.next() == XmlPullParser.TEXT) plan_type = xpp.getText();
					Data.plan_type=plan_type;
				}
				else if (xpp.getName().equals("PolicyNo") || xpp.getName().equals("a:PolicyNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) policyno = xpp.getText();
					Data.policyno=policyno;
				}
				else if (xpp.getName().equals("PreEx") || xpp.getName().equals("a:PreEx"))
				{
					if (xpp.next() == XmlPullParser.TEXT) pre_ex = xpp.getText();
					Data.pre_ex=pre_ex;
				}
				else if (xpp.getName().equals("Province") || xpp.getName().equals("a:Province"))
				{
					if (xpp.next() == XmlPullParser.TEXT) province = xpp.getText();
					Data.province=" "+province;
				}
				else if (xpp.getName().equals("Riders") || xpp.getName().equals("a:Riders"))
				{
					if (xpp.next() == XmlPullParser.TEXT) riders = xpp.getText();
					Data.riders=riders;
				}
				else if (xpp.getName().equals("RoomDescription") || xpp.getName().equals("a:RoomDescription"))
				{
					if (xpp.next() == XmlPullParser.TEXT) room_desc = xpp.getText();
					Data.room_desc=room_desc;
				}
				else if (xpp.getName().equals("RoomRate") || xpp.getName().equals("a:RoomRate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) room_rate = xpp.getText();
					Data.room_rate=room_rate;
					System.out.println("Room-----"+room_rate);
				}
				else if (xpp.getName().equals("Sex") || xpp.getName().equals("a:Sex"))
				{
					if (xpp.next() == XmlPullParser.TEXT) sex = xpp.getText();
					Data.sex=sex;
				}
				else if (xpp.getName().equals("Street") || xpp.getName().equals("a:Street"))
				{
					if (xpp.next() == XmlPullParser.TEXT) street = xpp.getText();
					Data.street=" "+street;
				}
			}
			eventType = xpp.next();
		}
		return successful_flag2;
	}
}
