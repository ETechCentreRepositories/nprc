package ngeeann.com.redcamp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.Objects;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Content.MainActivity;
import ngeeann.com.redcamp.Login.LaunchScreen;
import ngeeann.com.redcamp.Login.Login;
import ngeeann.com.redcamp.Login.LoginLauncher;

public class Splash extends AppCompatActivity {
    ImageView background;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        background = findViewById(R.id.background);
//        startBackgroundAnim();
        Glide.with(this).asGif().load(R.drawable.splash).into(background);

        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(() -> {

            sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);

            if (sessionManager.getString(SESSION_ID, null) == null) {
                Intent i = new Intent(Splash.this, LaunchScreen.class);
                Log.w("SESSION_ID:", "not logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
                startActivity(i);
                finish();
            } else if (sessionManager.getString(SESSION_ID, null).equals("404")) {
                Intent i = new Intent(Splash.this, LoginLauncher.class);
                Log.i("SESSION_ID ERROR:", "Logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
                startActivity(i);
                finish();
            } else if (sessionManager.getString(SESSION_ID, null).equals("400")) {
                Intent i = new Intent(Splash.this, LoginLauncher.class);
                Log.i("SESSION_ID ERROR:", "Logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
                startActivity(i);
                finish();
            } else if(sessionManager.getString(SESSION_ID, null).equals("200")){
                Intent i = new Intent(Splash.this, Home.class);
                Log.i("SESSION_ID ERROR:", "Logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
                startActivity(i);
                finish();
            } else {
                Log.e("SESSION_ID ERROR:", "Retrieved_ID ->" + sessionManager.getString(SESSION_ID, null));
            }

        }, SPLASH_TIME_OUT);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        startBackgroundAnim();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        startBackgroundAnim();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        stopBackgroundAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopBackgroundAnim();
    }


    private void startBackgroundAnim() {
        AnimationDrawable progressDrawable = (AnimationDrawable) background.getBackground();
        progressDrawable.start();
    }

    private void stopBackgroundAnim() {
        AnimationDrawable progressDrawable = (AnimationDrawable) background.getBackground();
        progressDrawable.stop();
    }
}
