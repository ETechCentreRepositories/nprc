package ngeeann.com.redcamp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ngeeann.com.redcamp.Content.Home;
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
    AppCompatCheckBox checkBox;

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        link = new Links();
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkbox);
        login.setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            if (checkEmpty()) {
                if (checkNetwork()) {
                    Boolean checkNetworkState = checkNetwork();
                    if (!checkNetworkState) {
                        progressbar.setVisibility(View.GONE);
                        login.setEnabled(true);
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
            return request.PostRequest(link.getLogin(), "email=" + email.getText().toString() + "&password=" + password.getText().toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            Log.e("JSON RETURN: ", s.toString());
            try {
                JSONObject result = new JSONObject(s);
                int status = result.getInt("status");
                String message = result.getString("message");
                Log.i("JSON MESSAGE:", message);
                Log.i("JSON STATUS: ", String.valueOf(status));
                if (status == 200) {
                    JSONArray users = result.getJSONArray("users");
                    JSONObject user = users.getJSONObject(0);
                    String name = user.getString("name");
                    String email = user.getString("email");
                    Log.i("USER NAME: ", name);
                    Log.i("USER EMAIL: ", email);
                    login.setEnabled(true);
                    if (checkBox.isChecked()){
                        //remember me
                        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sessionManager.edit();
                        editor.putString(SESSION_ID, "200");
                        editor.putString("email", email);
                        editor.putString("name", name);
                        editor.putString("dob", user.getString("dob"));
                        editor.apply();
                        startActivity(new Intent(Login.this, Home.class).putExtra("name", name).putExtra("email", email).putExtra("dob", user.getString("dob")));
                        finish();
                    }else{
                        startActivity(new Intent(Login.this, Home.class).putExtra("name", name).putExtra("email", email).putExtra("dob", user.getString("dob")));
                        finish();

                    }


                } else {
                    progressbar.setVisibility(View.GONE);
                    login.setEnabled(true);
                    Toast.makeText(Login.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public Boolean checkEmpty() {
        return !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty();
    }

}
