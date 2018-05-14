package ngeeann.com.redcamp;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class Splash extends AppCompatActivity {
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        background = findViewById(R.id.background);
//        startBackgroundAnim();
        Glide.with(this).asGif().load(R.drawable.splash).into(background);
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


    private void startBackgroundAnim(){
        AnimationDrawable progressDrawable = (AnimationDrawable) background.getBackground();
        progressDrawable.start();
    }
    private void stopBackgroundAnim(){
        AnimationDrawable progressDrawable = (AnimationDrawable) background.getBackground();
        progressDrawable.stop();
    }
}
