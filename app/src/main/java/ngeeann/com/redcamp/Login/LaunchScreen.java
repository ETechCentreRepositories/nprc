package ngeeann.com.redcamp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ngeeann.com.redcamp.R;

public class LaunchScreen extends AppCompatActivity {
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;
    Button btnLeggo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        btnLeggo = findViewById(R.id.id_continue_to_login_launcher);
        btnLeggo.setOnClickListener((View v) ->{
            sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sessionManager.edit();
            editor.putString(SESSION_ID, "404");
            editor.apply();
            startActivity(new Intent(this, LoginLauncher.class));
            finish();
        });
    }
}
