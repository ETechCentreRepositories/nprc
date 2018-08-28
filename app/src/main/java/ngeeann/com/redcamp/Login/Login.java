package ngeeann.com.redcamp.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Content.MainActivity;
import ngeeann.com.redcamp.Links;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.connection.HttpRequest;
import ngeeann.com.redcamp.connection.getAsyncRequest;

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
            return request.GetRequest(link.getLogin()+"?useremail="+email.getText().toString()+"&password="+password.getText().toString()+"&loginmethod=Normal");
            //return request.PostRequest(link.getLogin(), "useremail=" + email.getText().toString() + "&password=" + password.getText().toString() + "&loginmethod=Normal");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            //Log.e("JSON RETURN: ", s.toString());
            try {
//                JSONObject result = new JSONObject(s);
                JSONObject result = new JSONObject();
                result.put("status",200);
                result.put("message","Success");

                JSONArray tempUsers = new JSONArray();
                JSONObject tempUser = new JSONObject();
                tempUser.put("name","Admin");
                tempUser.put("email","admin@etech.com");
                tempUser.put("mobile","91712630");
                tempUser.put("dob","18-10-2000");
                tempUser.put("userStatus",1);
                tempUser.put("tribe","Ninja");
                tempUser.put("hasSignedConsent",true);
                tempUser.put("consentRequired",false);

                //hasPoll to be removed as it will be based on the date instead of db data
                tempUser.put("hasPoll",false);

                tempUsers.put(tempUser);
                result.put("users",tempUsers);
                result.put("display_title","Welcome");
                result.put("display_message","Welcome Admin");


                int status = result.getInt("status");
                String message = result.getString("message");
                Log.i("JSON MESSAGE:", message);
                Log.i("JSON STATUS: ", String.valueOf(status));
                if (status == 201) {
                    JSONArray users = result.getJSONArray("Users");
                    JSONObject user = users.getJSONObject(0);
                    String name = user.getString("userName");
                    String email = user.getString("userEmail");
                    Log.i("USER NAME: ", name);
                    Log.i("USER EMAIL: ", email);
                    int statuses_id = user.getInt("userStatus");
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

                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("token refresh", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            Log.d("token","asdasd");
                            Log.d("token refresh", token);
                        }
                    });

                    sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                    JSONArray users = result.getJSONArray("users");
                    JSONObject user = users.getJSONObject(0);
                    String name = user.getString("name");
                    String email = user.getString("email");
                    String mobile = user.getString("mobile");
                    String dob = user.getString("dob");
                    Boolean hasSignedConsent = user.getBoolean("hasSignedConsent");
                    Boolean consentRequired = user.getBoolean("consentRequired");
                    Boolean hasPoll = user.getBoolean("hasPoll");


                    SharedPreferences.Editor editor = sessionManager.edit();
                    editor.putString(SESSION_ID, "200");
                    editor.putString("email", email);
                    editor.putString("name", name);
                    editor.putString("number", mobile);
                    editor.putString("dob", dob);
                    editor.putBoolean("hasSignedConsent",hasSignedConsent);
                    editor.putBoolean("consentRequired",consentRequired);

                    //hasPoll to be removed as it will be based on the date instead of db data
                    editor.putBoolean("hasPoll",hasPoll);
                    if(user.has("tribe")){
                        String tribe = user.getString("tribe");
                        editor.putString("tribe",tribe);
                    }

                    editor.putBoolean("firstTime",true);
                    editor.putBoolean("showDialog",false);
                    editor.putBoolean("timedOut",false);
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
}
