package ngeeann.com.redcamp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ngeeann.com.redcamp.R;

public class LoginLauncher extends AppCompatActivity {
    Button login, signup, fbsignup, googlesignup;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_launcher);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        googlesignup = findViewById(R.id.googlesignup);
        fbsignup = findViewById(R.id.fbsignup);

        login.setOnClickListener((View v)->{
            startActivity(new Intent(this, Login.class));
            sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sessionManager.edit();
            editor.putString(SESSION_ID, "400");
            editor.apply();
        });
        signup.setOnClickListener((View v)->{
            startActivity(new Intent(this, Signup.class));
        });
        fbsignup.setOnClickListener((View v)->{
            startActivity(new Intent(this, Signup.class));
        });
        googlesignup.setOnClickListener((View v)->{
            startActivity(new Intent(this, Signup.class));
        });
    }
}
