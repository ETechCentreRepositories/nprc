package ngeeann.com.redcamp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.SQLiteQuestions.DatabaseHelper;
import ngeeann.com.redcamp.SQLiteQuestions.Question;
import ngeeann.com.redcamp.SQLiteQuestions.QuestionBank;

public class Splash extends AppCompatActivity {
    ImageView background;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;
    TextView hashKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hashKey = findViewById(R.id.hashkey);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ngeeann.com.redcamp" +
                            "",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        int SPLASH_TIME_OUT = 2000;
        QuestionBank qb = new QuestionBank();
        qb.initQuestions(getApplicationContext());
        getAllQuestions();
        new Handler().postDelayed(() -> {
 

            Intent i = new Intent(Splash.this, LoginLauncher.class);
            startActivity(i);
            finish();

//            if (sessionManager.getString(SESSION_ID, null) == null) {
//                Intent i = new Intent(Splash.this, LaunchScreen.class);
//                Log.w("SESSION_ID:", "not logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
//                startActivity(i);
//                finish();
//            } else if (sessionManager.getString(SESSION_ID, null).equals("404")) {
//                Intent i = new Intent(Splash.this, LoginLauncher.class);
//                Log.i("SESSION_ID ERROR:", "Logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
//                startActivity(i);
//                finish();
//            } else if (sessionManager.getString(SESSION_ID, null).equals("400")) {
//                Intent i = new Intent(Splash.this, LoginLauncher.class);
//                Log.i("SESSION_ID ERROR:", "Logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
//                startActivity(i);
//                finish();
//            } else if(sessionManager.getString(SESSION_ID, null).equals("200")){
//                Intent i = new Intent(Splash.this, Home.class);
//                Log.i("SESSION_ID ERROR:", "Logged in. ID ->" + sessionManager.getString(SESSION_ID, null));
//                startActivity(i);
//                finish();
//            } else {
//                Log.e("SESSION_ID ERROR:", "Retrieved_ID ->" + sessionManager.getString(SESSION_ID, null));
//            }

        }, SPLASH_TIME_OUT);

    }

    private void getAllQuestions()
    {

        Log.i("db row question","starting getresults()");

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        List<Question> list = dbHelper.getAllQuestionsList();


        Log.i("db row question",list.toString());

        //JSONArray resultSet  = new JSONArray();
         JSONObject resultSet  = new JSONObject();

        for(int i = 0; i < list.size(); i++){

            JSONObject row = new JSONObject();
            Question q = list.get(i);

            try{
                row.put("questionID",q.getQuestionID());
                row.put("tribe",q.getTribe());
                row.put("question",q.getQuestion());
                JSONArray options = new JSONArray();
                options.put(0,q.getOption(0));
                options.put(1,q.getOption(1));
                options.put(2,q.getOption(2));
                options.put(3,q.getOption(3));
                options.put(4,q.getOption(4));
                row.put("options",options);
                row.put("userAnswer",q.getUserAnswer());
                resultSet.put(q.getTribe(),row);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        Log.i("db row question",resultSet.toString());


        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sessionManager.edit();
        editor.putString("allQuestions", resultSet.toString());
        editor.commit();




        //Log.d("TAG_NAME", resultSet.toString() );
        //return resultSet;
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

}
