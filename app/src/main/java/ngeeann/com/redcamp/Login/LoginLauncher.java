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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;


import ngeeann.com.redcamp.Content.MainActivity;
import ngeeann.com.redcamp.R;

public class LoginLauncher extends AppCompatActivity {
    Button login, signup, fbsignup, googlesignup;
    private SignInButton googleButton;
    private FirebaseAuth mAuth;
    private static final String TAG = "Login Launcher";
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    //FB stuff
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton fblogin;


    boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_launcher);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        googlesignup = findViewById(R.id.googlesignup);
        googleButton = findViewById(R.id.googlelogin);
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginLauncher.this, "GOT ERROR", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        mAuthListener = firebaseAuth -> {


            if (firebaseAuth.getCurrentUser() != null) {
                String getName = firebaseAuth.getCurrentUser().getDisplayName();
                String getEmail = firebaseAuth.getCurrentUser().getEmail();
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(LoginLauncher.this, Signup.class)
                        .putExtra("method","google")
                        .putExtra("name",getName)
                        .putExtra("email",getEmail));
            }
        };
        googlesignup.setOnClickListener(v-> {
            if(checkNetwork()){

                signIn();
            }else{
                Toast.makeText(this, "Please switch on your data or wifi",Toast.LENGTH_SHORT).show();
            }

        });
//        googleButton.setOnClickListener(v -> );


        fbsignup = findViewById(R.id.fbsignup);

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


    private void signIn() {
        Intent signin = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signin, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }


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

        } else {
            Log.e("res: ", String.valueOf(requestCode));
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
