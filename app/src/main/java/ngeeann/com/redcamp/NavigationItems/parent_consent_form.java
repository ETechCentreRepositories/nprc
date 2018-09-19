package ngeeann.com.redcamp.NavigationItems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.R;

public class parent_consent_form extends AppCompatActivity {

    EditText etParentName, etParentMobile , etOthers;
    TextView etRelationship;
    //Spinner spnRelationship;
    Button btnSign;
    Toolbar toolbar;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_consent_form);

        etParentMobile = findViewById(R.id.etParentMobile);
        etParentName = findViewById(R.id.etParentName);
        etOthers = findViewById(R.id.etOthers);
        //spnRelationship = findViewById(R.id.relationship_spinner);
        btnSign = findViewById(R.id.btnSignhere);
        etRelationship = findViewById(R.id.etRelationship);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        //populate spinner
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.relationship_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnRelationship.setAdapter(adapter);

        etRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder b = new AlertDialog.Builder(parent_consent_form.this);
                b.setTitle("Select one of the following");
                String[] types = {"Mother", "Father","Grandparent","Sibling","Other"};
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etRelationship.setText(types[which]);
                        if(which == 4 && types[which].equalsIgnoreCase("Other")){
                            etOthers.setVisibility(View.VISIBLE);
                        } else {
                            etOthers.setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                    }

                });
                AlertDialog dialog = b.create();
                dialog.show();

            }
        });


        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etParentName.getText().toString().isEmpty()||etParentMobile.getText().toString().isEmpty()||etRelationship.getText().toString().equals("Relationship")){
                    Toast.makeText(parent_consent_form.this, "Please fill in all your details!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent in = new Intent(parent_consent_form.this,signature_activity.class)
                            .putExtra("name",etParentName.getText().toString())
                            .putExtra("mobile",etParentMobile.getText().toString())
                            .putExtra("relationship", etRelationship.getText().toString());
                    startActivity(in);
                }
            }
        });
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
                startActivity(new Intent(parent_consent_form.this, LoginLauncher.class));
                finish();
            }
        }
    }

}
