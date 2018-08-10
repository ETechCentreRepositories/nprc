package ngeeann.com.redcamp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import ngeeann.com.redcamp.Login.LoginLauncher;

public class AutoLogout extends Service {
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;
    public static CountDownTimer timer;
    @Override
    public void onCreate(){
        super.onCreate();
        //20mins countdown timer
        timer = new CountDownTimer(1 *5 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                //Some code
                Log.v("AutoLogout", "Service Started: " +  millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.v("AutoLogout", "Call Logout by Service");
                // Code for Logout
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putString(SESSION_ID, "400");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), LoginLauncher.class));
                stopSelf();
            }
        };
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
