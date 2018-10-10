package com.example.nprcregistrar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    TextView name, nric, parentConsent, mobile, tribe, eventday;
    Button register;


    private ZXingScannerView zXingScannerView;
    int REQUEST_CAMERA = 0;
    LinearLayout camera, progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        nric = findViewById(R.id.nric);
        parentConsent = findViewById(R.id.parentconsent);
        mobile = findViewById(R.id.number);
        tribe = findViewById(R.id.tribe);
        register = findViewById(R.id.register);
        eventday = findViewById(R.id.day);

        progressbar = findViewById(R.id.progressbar);
        setProgressbar(false);

        int day = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        if (date.equals("20-10-2018") || date.equals("21-10-2018") || date.equals("22-10-2018")) {
            if (date.equals("20-10-2018")) {
                day = 1;
            } else if (date.equals("21-10-2018")) {
                day = 2;
            } else if (date.equals("22-10-2018")) {
                day = 3;
            } else {
                day = 0;
            }
        } else {
            day = 0;
        }
        eventday.setText(String.format("DAY %s", String.valueOf(day)));

//        final int finalDay = day;


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        camera = findViewById(R.id.camera);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Camera permission is needed to scan E-Ticket", Toast.LENGTH_SHORT).show();
            }

            if (android.os.Build.VERSION.SDK_INT > 23) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }

        } else {
            zXingScannerView = new ZXingScannerView(getApplicationContext());
//            setContentView(zXingScannerView);
            camera.addView(zXingScannerView);
            StartCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                zXingScannerView = new ZXingScannerView(getApplicationContext());
//                setContentView(zXingScannerView);
                camera.addView(zXingScannerView);
                StartCamera();
            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StartCamera();
    }

    @Override
    public void handleResult(Result result) {
//        Intent i = new Intent(MainActivity.this, ScanDetails.class);
        String stringProfile = null;
        register.setVisibility(View.VISIBLE);
        try {
            stringProfile = AESCrypt.decrypt("password", result.getText());
            JSONObject jsonProfile = null;
            jsonProfile = new JSONObject(stringProfile);
            final int studentID = Integer.parseInt(jsonProfile.getString("id"));
            name.setText(jsonProfile.getString("name"));
            nric.setText(jsonProfile.getString("nric"));
            mobile.setText(jsonProfile.getString("mobile"));
            tribe.setText(jsonProfile.getString("tribe"));
            if (jsonProfile.getBoolean("parentConsent")) {
                parentConsent.setText("Yes");
            } else {
                parentConsent.setText("No");
            }

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int day = Integer.parseInt(eventday.getText().toString().split(" ")[1]);
                    if (day == 1 || day == 2 || day == 3) {
                        String getName = name.getText().toString();
                        String getNric = nric.getText().toString();
                        String getMobile = mobile.getText().toString();
                        String getTribe = tribe.getText().toString();
                        String getPermission = parentConsent.getText().toString();
                        if (getName.equals("") || getName.equals("Not Added") || getNric.equals("") || getNric.equals("Not Added") || getMobile.equals("") || getMobile.equals("Not Added")) {
                            Toast.makeText(MainActivity.this, "No details to add", Toast.LENGTH_SHORT).show();
                        } else {
                            if (getTribe.equals("") || getTribe.equals("Not assigned yet")) {
                                Toast.makeText(MainActivity.this, "User does not have a tribe yet!\nPlease assist!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (getPermission.equals("") || getPermission.equals("No")) {
                                    Toast.makeText(MainActivity.this, "User does not have parental permission!", Toast.LENGTH_SHORT).show();
                                } else {
                                    setProgressbar(true);
                                    Register register = new Register();
                                    try {
                                        String details = register.execute(String.valueOf(studentID), String.valueOf(day)).get();
                                        if (details == null) {
                                            Toast.makeText(MainActivity.this, "An unexpected error has occurred", Toast.LENGTH_SHORT).show();
                                            StartCamera();
                                        } else {
                                            JSONObject result = new JSONObject(details);
                                            int status = result.getInt("status");
                                            if (status == 200) {
                                                Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            } else if (status == 202) {
                                                Toast.makeText(MainActivity.this, "Student has already registered for today's event", Toast.LENGTH_SHORT).show();
                                            } else if (status == 403) {
                                                Toast.makeText(MainActivity.this, "Attendance cannot be saved.\nPlease try again\nor contact the admin.", Toast.LENGTH_SHORT).show();
                                            } else if (status == 404) {
                                                Toast.makeText(MainActivity.this, "Cannot find user details.\nPlease check with the admin", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, "An unexpected error has occurred.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "An unexpected error has occurred.", Toast.LENGTH_SHORT).show();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "An unexpected error has occurred.", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "An unexpected error has occurred.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "There are no events today!", Toast.LENGTH_SHORT).show();
                    }
                    StartCamera();
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        i.putExtra("results",result.getText());
//        startActivity(i);
        //zXingScannerView.resumeCameraPreview(this);
    }

    public class Register extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String link = "https://www1.np.edu.sg/npnet/MobileApi/api/Attend/postattend/?userid=" + strings[0] + "&day=" + strings[1];
            HttpRequest sendRequest = new HttpRequest();
            String params = "";
//            return "{'status'=200}"; // success
//            return "{'status'=202}"; // user already registered
//            return "{'status'=403}"; // user cannot register (sql failed update)
//            return "{'status'=404}"; // user cannot be found
            return sendRequest.PostRequest(link, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgressbar(false);
            name.setText("");
            nric.setText("");
            parentConsent.setText("");
            mobile.setText("");
            tribe.setText("");
        }
    }

    public void setProgressbar(Boolean b) {
        if (b) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.GONE);
        }
    }

    public void StartCamera() {
        register.setVisibility(View.GONE);
        zXingScannerView.stopCamera();
        zXingScannerView.setResultHandler(MainActivity.this);
        zXingScannerView.setAutoFocus(true);
        zXingScannerView.startCamera();
    }
}