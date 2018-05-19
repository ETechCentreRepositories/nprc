package ngeeann.com.redcamp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;


import ngeeann.com.redcamp.Content.MainActivity;
import ngeeann.com.redcamp.R;

public class LoginLauncher extends AppCompatActivity {
    Button login, signup, fbsignup, googlesignup;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    public static final String TAG = "LoginLauncher.java";
    SharedPreferences sessionManager;

    //FB stuff
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton fblogin;

    boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_launcher);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        //Bryan's
        googlesignup = findViewById(R.id.googlesignup);

        googlesignup.setOnClickListener(v -> {
            Log.d(TAG, "setOnClickListener: ");
            signIn();
        });

        fbsignup = findViewById(R.id.fbsignup);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //fb setup
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        fblogin = findViewById(R.id.fblogin);
        fblogin.setReadPermissions(Arrays.asList(EMAIL));
            // If you are using in a fragment, call loginButton.setFragment(this);

            // Callback registration
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    getUserDetails(loginResult);
                }

                @Override
                public void onCancel() {
                    // App code
                }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        login.setOnClickListener((View v) -> {
            startActivityForResult(new Intent(this, Login.class), 1);
            sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sessionManager.edit();
            editor.putString(SESSION_ID, "400");
            editor.apply();
        });
        signup.setOnClickListener((View v) -> {
            startActivity(new Intent(this, Signup.class).putExtra("method", "normal"));
        });
        fbsignup.setOnClickListener((View v) -> {
//            startActivity(new Intent(this, Signup.class));
            if (checkNetwork()) {
                fblogin.performClick();
            }

        });

    }

    private void signIn() {
        Log.d(TAG, "signIn: ");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (json_object, response) -> {
                    Log.wtf("jsonString: ", "" + json_object);
                    Intent intent = new Intent(LoginLauncher.this, Signup.class);
                    intent.putExtra("userProfile", json_object.toString());
                    intent.putExtra("method", "facebook");
                    LoginManager.getInstance().logOut();
                    startActivity(intent);
                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
    }

    public boolean checkNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert conMgr != null;
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            // notify user you are online
            return true;

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online
            Toast.makeText(this, "Please switch on your data or wifi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Log.e("res: ", String.valueOf(requestCode));
            if (data != null) {
                if (data.getStringExtra("type") != null) {
                    if (data.getStringExtra("type").equals("0")) {
                        Log.e("type", "back button");
                    } else if (data.getStringExtra("type").equals("1")) {
                        finish();
                    }
                } else {
                    Log.e("type", "null");
                }
                Log.e("data", "null");
            }

        } else if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: RC_SIGN_IN");
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult: ");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            account.getEmail();
            account.getDisplayName();


            Log.d(TAG, "handleSignInResult: successful " + account);

//            Intent intent = new Intent(LoginLauncher.this,)

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
