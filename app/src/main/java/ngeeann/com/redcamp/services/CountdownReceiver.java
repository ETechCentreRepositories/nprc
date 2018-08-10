package ngeeann.com.redcamp.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Login.LoginLauncher;

public class CountdownReceiver extends Service {

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(1 *5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Call Logout by Service");
                // Code for Logout
                Toast.makeText(getApplicationContext(),"Logging out",Toast.LENGTH_SHORT).show();
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.clear();
                editor.putBoolean("timedOut", true);
                editor.putBoolean("showDialog", true);
                editor.apply();
//                startActivity(new Intent(getApplicationContext(), LoginLauncher.class));
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
