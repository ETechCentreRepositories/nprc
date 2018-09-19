package ngeeann.com.redcamp.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Links;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.connection.HttpRequest;

public class Login extends AppCompatActivity {
    Button login;
    EditText email, password;
    Links link;
    Toolbar toolbar;
    LinearLayout progressbar;
    Button forgetpw;

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        link = new Links();
        setContentView(R.layout.activity_login);
        forgetpw = findViewById(R.id.forgotpassword);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
            Intent ib = new Intent();
            ib.putExtra("type", "0");
            setResult(1, ib);
            finish();
        });
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        forgetpw.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPassword.class).putExtra("email", email.getText().toString()));
        });

        login.setOnClickListener(v -> {

            progressbar.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            if (checkEmpty()) {
                if (checkNetwork()) {
                    Boolean checkNetworkState = checkNetwork();
                    if (!checkNetworkState) {
                        Toast.makeText(Login.this, "Please Switch on your wifi or Data", Toast.LENGTH_SHORT).show();

                    } else {
                        DoLogin login = new DoLogin();
                        login.execute(link.getLogin());
                    }
                }
            } else {
                progressbar.setVisibility(View.GONE);
                login.setEnabled(true);
                Toast.makeText(Login.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            // notify user you are online
            return true;

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            progressbar.setVisibility(View.GONE);
            login.setEnabled(true);

            // notify user you are not online
            Toast.makeText(this, "Please Switch your data on", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private class DoLogin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpRequest request = new HttpRequest();
            link = new Links();
            String loginresult = request.GetRequest(link.getLogin() + "?useremail=" + email.getText().toString() + "&password=" + password.getText().toString() + "&loginmethod=Normal");
            Log.i("RESULT LOGIN", loginresult);
            try {
                JSONObject result = new JSONObject(loginresult);
                int status = result.getInt("status");
                JSONObject user = result.getJSONObject("User");
                String userid = user.getString("userId");
                if (status == 200) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("123token refresh", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            Log.d("123token", "asdasd");
                            Log.d("123token refresh", token);
                            updatetoken updatetoken = new updatetoken();
                            String updateFCMToken = null;
                            try {
                                updateFCMToken = updatetoken.execute(userid, token).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Log.e("UPDATE TOKEN DIB", updateFCMToken);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return loginresult;
            //return request.PostRequest(link.getLogin(), "useremail=" + email.getText().toString() + "&password=" + password.getText().toString() + "&loginmethod=Normal");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            //Log.e("JSON RETURN: ", s.toString());
            try {
//                JSONObject result = new JSONObject(s);
                JSONObject result = new JSONObject(s);

                int status = result.getInt("status");
                String message = result.getString("message");
                Log.i("JSON MESSAGE:", message);
                Log.i("JSON STATUS: ", String.valueOf(status));
                if (status == 201) {
//                    JSONArray users = result.getJSONArray("User");
//                    JSONArray users = result.getJSONArray("User");
                    JSONObject user = result.getJSONObject("User");
                    String name = user.getString("userName");
                    String email = user.getString("userEmail");
                    Log.i("USER NAME: ", name);
                    Log.i("USER EMAIL: ", email);
                    String statuses_id = user.getString("userStatus");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    dialog.setCancelable(false);
                    dialog.setTitle(result.getString("display_title"));
                    dialog.setMessage(result.getString("display_message"));
                    dialog.setPositiveButton("OK", (dialogInterface, i) -> {
                    });
                    AlertDialog dialogue = dialog.create();
                    dialogue.show();
                    login.setEnabled(true);
                } else if (status == 200) {
                    login.setEnabled(true);


                    sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                    JSONObject user = result.getJSONObject("User");
                    String id = user.getString("userId");
                    String name = user.getString("userName");
                    String email = user.getString("userEmail");
                    String mobile = user.getString("userMobile");
                    String nric = user.getString("userNric");
                    String dob = user.getString("userDob");
                    String hasSignedConsent = user.getString("parentSign");
                    String consentRequired = user.getString("parentEnable");
////                    if(user.get("parentSign")!=null){
////                        hasSignedConsent = user.getBoolean("parentSign");
////                    }else{
////                        hasSignedConsent = false;
//                    }
                    SharedPreferences.Editor editor = sessionManager.edit();
                    editor.putString(SESSION_ID, "200");
                    editor.putString("id", id);
                    editor.putString("email", email);
                    editor.putString("name", name);
                    editor.putString("contact", mobile);
                    editor.putString("nric", nric);
                    editor.putString("dob", dob);
                    editor.putString("hasSignedConsent", hasSignedConsent);
                    editor.putString("consentRequired", consentRequired);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    String date = dateFormat.format(Calendar.getInstance().getTime());
                    Log.e("date", date);
                    Boolean hasPoll = false;
                    if(date.equals("20-10-2018")||date.equals("21-10-2018")||date.equals("22-10-2018")){
                        hasPoll = true;
                    }else{
                        hasPoll = false;
                    }
                    //hasPoll to be removed as it will be based on the date instead of db data
                    editor.putBoolean("hasPoll",hasPoll);
                    if (user.has("userTribe")) {
                        String tribe = user.getString("userTribe");
                        editor.putString("tribe", tribe);
                    }

                    editor.putBoolean("firstTime", true);
                    editor.putBoolean("showDialog", false);
                    editor.putBoolean("timedOut", false);
                    editor.apply();
                    Intent ib = new Intent();
                    ib.putExtra("type", "1");
                    setResult(1, ib);
                    //finish();
//                    Intent forceLogoffIntent = new Intent(ForceOfflineReceiver.ACTION_FORCE_OFFLINE);
//                    sendBroadcast(forceLogoffIntent);
                    startActivity(new Intent(Login.this, Home.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("name", name));
                    //.putExtra("email", email)
                    //.putExtra("number", user.getString("mobile"))
                    //.putExtra("dob", user.getString("dob")));
                    finish();
                } else if (status == 203) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    dialog.setCancelable(false);
                    dialog.setTitle(result.getString("display_title"));
                    dialog.setMessage(result.getString("display_message"));
                    dialog.setPositiveButton("OK", (dialogInterface, i) -> {
                    });
                    AlertDialog dialogue = dialog.create();
                    dialogue.show();
                    login.setEnabled(true);


                } else if (status == 401) {
                    progressbar.setVisibility(View.GONE);
                    login.setEnabled(true);
                    Toast.makeText(Login.this, result.getString("display_message"), Toast.LENGTH_SHORT).show();
                } else if (status == 404) {
                    progressbar.setVisibility(View.GONE);
                    login.setEnabled(true);
                    Toast.makeText(Login.this, result.getString("display_message"), Toast.LENGTH_SHORT).show();
                } else {
                    progressbar.setVisibility(View.GONE);
                    login.setEnabled(true);
                    Toast.makeText(Login.this, result.getString("display_message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public Boolean checkEmpty() {
        return !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ib = new Intent();
        ib.putExtra("type", "0");
        setResult(1, ib);
        finish();
    }

    public class updatetoken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpRequest request1 = new HttpRequest();
            Links links = new Links();
            String updateFCMToken = request1.PostRequest(links.getUpdateFCMToken() + "?userid=" + strings[0] + "&token=" + strings[1], "");
            return updateFCMToken;
        }
    }
}
