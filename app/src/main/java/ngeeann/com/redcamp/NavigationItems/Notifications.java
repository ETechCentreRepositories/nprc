package ngeeann.com.redcamp.NavigationItems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.NotificationDetails;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.SQLiteQuestions.Notification;
import ngeeann.com.redcamp.connection.HttpRequest;

public class Notifications extends AppCompatActivity {

    Toolbar toolbar;
    CardView cv3;
    LinearLayout notificationLayout;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        notificationLayout = findViewById(R.id.layoutNotifications);
        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);


        try {
            updateNotificationUI(new JSONObject(new getnotifications().execute(sessionManager.getString("id", "")).get()).getJSONArray("Notification"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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

    private void updateNotificationUI(JSONArray details) throws JSONException {
        int counter = 0;

        ArrayList<Notification> list = new ArrayList<Notification>();
        for (int j = 0; j < details.length(); j++) {
            JSONObject noti = details.getJSONObject(j);
            list.add(new Notification(noti.getInt("noti_id"), noti.getString("noti_type"), "false", noti.getString("title"), noti.getString("message")));
        }
        Log.i("Notifications List: ", list.toString());
        //updateAmountOfNewNotification();
        notificationLayout.removeAllViews();
        for (int n = 0; n < list.size(); n++) {
            int i = updateRow(list.get(n));

            counter = counter + i;
        }
        updateAmountOfNewNotification(counter);

    }

    private void updateAmountOfNewNotification(int counter) {
        TextView tvAmount = findViewById(R.id.tvAmount);
        //DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        //int newAmount = dbHelper.getNumberOfNewNotification();
        tvAmount.setText(counter + " NEW");
    }


    private int updateRow(Notification notification) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.notification_row, null);

        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView ivArrow = view.findViewById(R.id.ivArrow);
        ImageView ivCircle = view.findViewById(R.id.ivRedCircle);
        CardView cvNotification = view.findViewById(R.id.cv1);

//        tvTime.setText(notification.getDatetimeReceived());
        tvTitle.setText(notification.getTitle());
        String isRead = notification.getIsRead();
        if (isRead.equalsIgnoreCase("false")) {
            ivCircle.setVisibility(View.VISIBLE);
            ivArrow.setVisibility(View.GONE);
        } else {
            ivCircle.setVisibility(View.GONE);
            ivArrow.setVisibility(View.VISIBLE);
        }

        cvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notifications.this, NotificationDetails.class);
                Notification n = notification;
                intent.putExtra("id", n.getNotificationID());
                intent.putExtra("type", n.getType());
                intent.putExtra("title", n.getTitle());
                intent.putExtra("isRead", n.getIsRead());
                intent.putExtra("message", n.getMessage());
                intent.putExtra("dateReceived", n.getDatetimeReceived());
                startActivity(intent);
            }
        });

        notificationLayout.addView(view);
//
        if (isRead.equalsIgnoreCase("false")) {
            return 1;
        } else {
            return 0;
        }

//        Long timeInMillis = Long.parseLong(notification.getDatetimeReceived());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeInMillis);
//        String month = calendar.get(Calendar.MONTH);


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTimeOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void checkTimeOut() {
        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        if (sessionManager.contains("timedOut")) {
            if (sessionManager.getBoolean("timedOut", false)) {
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putString(SESSION_ID, "400");
                editor.apply();
                startActivity(new Intent(Notifications.this, LoginLauncher.class));
                finish();
            }
        }
    }

    public class getnotifications extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpRequest request = new HttpRequest();
            String sendrequest = request.GetRequest("https://www1dev.np.edu.sg/npnet/MobileApi/api/Notification/getUserNotification?userid=" + strings[0]);
            Log.i("NOTIFICATION REQUEST", sendrequest);
            return sendrequest;
        }
    }
}
