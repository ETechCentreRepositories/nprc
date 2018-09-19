package ngeeann.com.redcamp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.SQLiteQuestions.DatabaseHelper;

public class NotificationDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvDatetime, tvTitle, tvMessage;
    int notificationID;
    ImageView ivBanner;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;
//    List<String> allBanners = Arrays.asList(
//            "Apaches.jpg", "Centurions.jpg","Spartans.jpg","Vikings.jpg","Ninjas.jpg",
//            "Mascots-buses.jpg","RC-Main.jpg","RC-Thumbs-up.jpg","RC-angbao.jpg",
//            "RC-angbao-withMascots.jpg","Jio-Your-Friends.jpg","Find-Your-Tribe.jpg",
//            "Power-up.jpg","telegram.jpg","Sign-Up.jpg",
//            "Countdown-1","countdown-2","countdown-3","countdown-4","Countdown-5",
//
//    );
    List<Integer> allBanners = Arrays.asList(
            R.drawable.apache_banner, R.drawable.centurion_banner, R.drawable.spartan_banner, R.drawable.viking_banner,
            R.drawable.ninja_banner, R.drawable.mascots_buses, R.drawable.rc_main, R.drawable.rc_thumbs_up,
            R.drawable.rc_angbao, R.drawable.rc_angbao_withmascots, R.drawable.jio_your_friends,
            R.drawable.find_your_tribe, R.drawable.power_up, R.drawable.telegram, R.drawable.sign_up,
            R.drawable.countdown_1, R.drawable.countdown_2, R.drawable.countdown_3, R.drawable.countdown_4,
            R.drawable.countdown_5
    );

    Map<String, Integer> allBanners2 = new HashMap<String, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvDatetime = findViewById(R.id.tvDatetime);
        tvTitle = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        ivBanner = findViewById(R.id.ivBanner);

        Intent intent = getIntent();
        String id = String.valueOf(intent.getIntExtra("id",0));
        String title = intent.getStringExtra("title");
        String datetime = intent.getStringExtra("dateReceived");
        String message = intent.getStringExtra("message");
        String type = intent.getStringExtra("type");
        String isRead = intent.getStringExtra("isRead");
        Log.i("intent value",id + ", " + title + ", " + datetime + ", " + message + ", " + type + ", " + isRead);

        initialiseAllBanners();
        notificationID = Integer.parseInt(id);
        tvTitle.setText(title);
        tvMessage.setText(message);
        tvDatetime.setText(datetime);
        if(type != null){
            ivBanner.setImageResource(allBanners.get(5));

        } else {
            Log.i("null","type is null");
        }

        if(isRead.equalsIgnoreCase("false")){
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.updateIsRead(id);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.share:
                shareIt();
                return true;

        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_share_diag, menu);


        return true;
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "I'm in for the MOST LIT Camp for O-levelers! \n" +
                "Join me at RED CAMP 15!  \n" +
                "\n" +
                "Download the RED CAMP App now to register!\n" +
                "\n" +
                "https://www.np.edu.sg/redcamp/Pages/default.aspx#register";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        //sharing implementation here

    }

    private void initialiseAllBanners(){
        allBanners2.clear();
        allBanners2.put("apache",R.drawable.apache_banner);
        allBanners2.put("centurion",R.drawable.centurion_banner);
        allBanners2.put("spartan",R.drawable.centurion_banner);
        allBanners2.put("viking",R.drawable.centurion_banner);
        allBanners2.put("ninja",R.drawable.centurion_banner);
        allBanners2.put("mascots_buses",R.drawable.mascots_buses);
        allBanners2.put("rc_main",R.drawable.rc_main);
        allBanners2.put("rc_thumbs_up",R.drawable.rc_thumbs_up);
        allBanners2.put("rc_angbao",R.drawable.rc_angbao);
        allBanners2.put("rc_angbao_withmascots",R.drawable.rc_angbao_withmascots);
        allBanners2.put("jio_your_friends",R.drawable.jio_your_friends);
        allBanners2.put("find_your_tribe",R.drawable.find_your_tribe);
        allBanners2.put("power_up",R.drawable.power_up);
        allBanners2.put("telegram",R.drawable.telegram);
        allBanners2.put("sign_up",R.drawable.sign_up);
        allBanners2.put("countdown_1",R.drawable.countdown_1);
        allBanners2.put("countdown_2",R.drawable.countdown_2);
        allBanners2.put("countdown_3",R.drawable.countdown_3);
        allBanners2.put("countdown_4",R.drawable.countdown_4);
        allBanners2.put("countdown_5",R.drawable.countdown_5);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkTimeOut();
    }

    private void checkTimeOut(){
        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        if(sessionManager.contains("timedOut")){
            if(sessionManager.getBoolean("timedOut",false)){
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putString(SESSION_ID, "400");
                editor.apply();
                startActivity(new Intent(NotificationDetails.this, LoginLauncher.class));
                finish();
            }
        }
    }
}
