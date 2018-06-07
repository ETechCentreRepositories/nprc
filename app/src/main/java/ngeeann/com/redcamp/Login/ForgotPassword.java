package ngeeann.com.redcamp.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import ngeeann.com.redcamp.Links;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.connection.HttpRequest;

public class ForgotPassword extends AppCompatActivity {
    Toolbar toolbar;
    EditText getemail;
    Button done;
    Links links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        getemail = findViewById(R.id.getemail);
        done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            links = new Links();
            DoForgetPassword forgetPassword = new DoForgetPassword();
            if (checkNetwork()) {
                forgetPassword.execute(links.getForgetpassword(), getemail.getText().toString());
            }
        });

    }

    public class DoForgetPassword extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpRequest request = new HttpRequest();
            return request.PostRequest(strings[0], "email=" + strings[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("FORGET PASSWORD RESULT:", s);
            try {
                JSONObject result = new JSONObject(s);
                int status = result.getInt("status");
                if (status == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setMessage(result.getString("message"));
                    builder.setPositiveButton("ok", (dialog, which) -> finish());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if(status == 404){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setMessage("You have not registered yet");
                    builder.setPositiveButton("ok", (dialog, which) -> finish());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if(status == 400){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setMessage(result.getString("message"));
                    builder.setPositiveButton("ok", (dialog, which) -> finish());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
}
