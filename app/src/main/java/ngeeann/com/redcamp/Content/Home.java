package ngeeann.com.redcamp.Content;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import ngeeann.com.redcamp.HomePage;
import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.Login.Signup;
import ngeeann.com.redcamp.R;

public class Home extends AppCompatActivity {

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    FragmentTransaction ft;
    TextView name, email, number, dob;
    LinearLayout logout;
    DrawerLayout drawerLayout;
    SharedPreferences sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.phone);
        dob = findViewById(R.id.dob);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
            dialog.setCancelable(false);
            dialog.setTitle("Logout");
            dialog.setMessage("Are you sure you want to log out?");
            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putString(SESSION_ID, "400");
                editor.apply();
                startActivity(new Intent(this, LoginLauncher.class));
                finish();
            });
            dialog.setNegativeButton("No", (dialogInterface, i) -> {

            });
            AlertDialog dialogue = dialog.create();
            dialogue.show();

        });

        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);

        Intent getintent = getIntent();
        String fullname = getintent.getStringExtra("name");
        if (fullname != null) {
            String getName = fullname;
            String getEmail = getIntent().getStringExtra("email");
            String getNumber = getIntent().getStringExtra("number");
            String getdob = getIntent().getStringExtra("dob");

            name.setText(getName);
            email.setText(getEmail);
            number.setText(getNumber);
            dob.setText(getdob);

        } else {
            String getName = sessionManager.getString("name", null);
            String getEmail = sessionManager.getString("email", null);
            String getNumber = sessionManager.getString("number", null);
            String getdob = sessionManager.getString("dob", null);

            name.setText(getName);
            email.setText(getEmail);
            number.setText(getNumber);
            dob.setText(getdob);
        }

        NavigationView navigationView = findViewById(R.id.left_drawer);
        replacefragment(new HomePage());
        configureToolbar();
        configureNavigationDrawer();
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

}
