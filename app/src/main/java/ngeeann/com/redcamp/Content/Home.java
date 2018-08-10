package ngeeann.com.redcamp.Content;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ngeeann.com.redcamp.HomePage;
import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.NavigationItems.Notifications;
import ngeeann.com.redcamp.NavigationItems.signature_activity;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.NavigationItems.parent_consent_form;
import ngeeann.com.redcamp.services.AutoLogout;
import ngeeann.com.redcamp.services.CountdownReceiver;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    FragmentTransaction ft;
    TextView name, email, number, dob;
    LinearLayout logout;
    DrawerLayout drawerLayout;
    SharedPreferences sessionManager;
    NavigationView navigationView;
    Boolean hasNewNotifications = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);

        if(sessionManager.contains("firstTime")){
            if(sessionManager.getBoolean("firstTime",false)){
                Log.i("BroadcastService","in Home class");
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putBoolean("firstTime",false);
                editor.apply();
                startService(new Intent(this, CountdownReceiver.class));
            }
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        name = findViewById(R.id.name);
//        email = findViewById(R.id.email);
//        number = findViewById(R.id.phone);
//        dob = findViewById(R.id.dob);
//        logout = findViewById(R.id.logout);
//        logout.setOnClickListener(v -> {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
//            dialog.setCancelable(false);
//            dialog.setTitle("");
//            dialog.setMessage("Are you sure you want to log out?");
//            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
//                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sessionManager.edit();
//                editor.putString(SESSION_ID, "400");
//                editor.apply();
//                startActivity(new Intent(this, LoginLauncher.class));
//                finish();
//            });
//            dialog.setNegativeButton("No", (dialogInterface, i) -> {
//
//            });
//            AlertDialog dialogue = dialog.create();
//            dialogue.show();
//
//        });


        Intent getintent = getIntent();
        String fullname = getintent.getStringExtra("name");
        if (fullname != null) {
            String getName = fullname;
            String getEmail = getIntent().getStringExtra("email");
            String getNumber = getIntent().getStringExtra("number");
            String getdob = getIntent().getStringExtra("dob");

//            name.setText(getName);
//            email.setText(getEmail);
//            number.setText(getNumber);
//            dob.setText(getdob);

        } else {
            String getName = sessionManager.getString("name", null);
            String getEmail = sessionManager.getString("email", null);
            String getNumber = sessionManager.getString("number", null);
            String getdob = sessionManager.getString("dob", null);

//            name.setText(getName);
//            email.setText(getEmail);
//            number.setText(getNumber);
//            dob.setText(getdob);
        }

        navigationView = findViewById(R.id.left_drawer);
        replacefragment(new HomePage());
        configureToolbar();
        configureNavigationDrawer();

        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.left_drawer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            // Android home
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    CloseDrawer();
                    return true;
                } else {
                    OpenDrawer();
                    return true;
                }
                // manage other entries if you have it ...
            case R.id.share:
                shareIt();
                return true;

        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);


        return true;
    }

    public void replacefragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void CloseDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void OpenDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();

        switch(itemid){
            case R.id.notifications:
//                Links links = new Links();
//                Intent intent = new Intent(Home.this, WebView.class);
//                intent.putExtra("Links", links.getCourse_finder());
//                intent.putExtra("name","COURSE FINDER");
//                startActivity(intent);
//                if(hasNewNotifications){
//                    hasNewNotifications = false;
//                } else {
//                    hasNewNotifications = true;
//                }
//                LinearLayout badgeLayout = (LinearLayout) navigationView.getMenu().findItem(R.id.notifications).getActionView();
//                ImageView ivAlert = badgeLayout.findViewById(R.id.ivNotificationAlert);
//                if(ivAlert.getVisibility() == View.INVISIBLE){
//                    ivAlert.setVisibility(View.VISIBLE);
//                } else {
//                    ivAlert.setVisibility(View.INVISIBLE);
//                }
//                return true;
                Intent notificationsIntent = new Intent(Home.this,Notifications.class);
                startActivity(notificationsIntent);
                return true;
            case R.id.parentConsent:
                Intent consentFormIntent = new Intent(Home.this,parent_consent_form.class);
                startActivity(consentFormIntent);
                return true;
            case R.id.qrcode:
                AlertDialog.Builder dialogQR = new AlertDialog.Builder(Home.this);
                LayoutInflater li = LayoutInflater.from(Home.this);
                final View gtnc = li.inflate(R.layout.dialog_qr_code ,null);
                dialogQR.setCancelable(true);
                dialogQR.setView(gtnc);
                AlertDialog dialogueQR = dialogQR.create();
                dialogueQR.show();
            case R.id.logout:
                AlertDialog.Builder dialogLogout = new AlertDialog.Builder(Home.this);
                dialogLogout.setCancelable(false);
                dialogLogout.setTitle("");
                dialogLogout.setMessage("Are you sure you want to log out?");
                dialogLogout.setPositiveButton("Yes", (dialogInterface, i) -> {
                    sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sessionManager.edit();
                    editor.clear();
                    editor.putString(SESSION_ID, "400");
                    editor.apply();
                    startActivity(new Intent(this, LoginLauncher.class));
                    finish();
                });
                dialogLogout.setNegativeButton("No", (dialogInterface, i) -> {

                });
                AlertDialog dialogueLogout = dialogLogout.create();
                dialogueLogout.show();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        if(sessionManager.contains("timedOut")){
            if(sessionManager.getBoolean("timedOut",false)){
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putString(SESSION_ID, "400");
                editor.apply();
                startActivity(new Intent(Home.this, LoginLauncher.class));
                finish();
            }
        }

    }



}
