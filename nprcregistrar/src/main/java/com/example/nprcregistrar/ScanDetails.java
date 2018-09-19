package com.example.nprcregistrar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

public class ScanDetails extends AppCompatActivity {

    Button btnRegister, btnHome;
    TextView tvName, tvMobile, tvNric, tvConsent, tvTribe;
    int studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnRegister = findViewById(R.id.btnRegister);
        btnHome = findViewById(R.id.btnHome);
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvNric = findViewById(R.id.tvNric);
        tvTribe = findViewById(R.id.tvtribe);
        tvConsent = findViewById(R.id.tvConsent);



        Intent i = getIntent();
        try{
            Bundle bd = i.getExtras();
            String getResults = (String) bd.get("results");
            Log.d("getResults",getResults);
            String stringProfile = AESCrypt.decrypt("password",getResults);
            Log.d("getResults",stringProfile);
            JSONObject jsonProfile = new JSONObject(stringProfile);
            studentID = Integer.parseInt(jsonProfile.getString("id"));
            tvName.setText(jsonProfile.getString("name"));
            tvNric.setText(jsonProfile.getString("nric"));
            tvMobile.setText(jsonProfile.getString("mobile"));
            tvTribe.setText(jsonProfile.getString("tribe"));
            if(jsonProfile.getBoolean("parentConsent")){
                tvConsent.setText("Yes");
            } else {
                tvConsent.setText("No");
            }
        } catch (GeneralSecurityException e){
            e.printStackTrace();
        }  catch (JSONException e ){
            e.printStackTrace();
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Registering student with id " + studentID, Toast.LENGTH_SHORT).show();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanDetails.this,MainActivity.class));
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScanDetails.this,MainActivity.class));
        finish();
    }
}
