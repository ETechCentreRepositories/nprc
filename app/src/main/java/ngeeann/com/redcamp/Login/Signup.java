package ngeeann.com.redcamp.Login;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Content.MainActivity;
import ngeeann.com.redcamp.Content.OcrCaptureActivity;
import ngeeann.com.redcamp.Links;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.connection.HttpRequest;

public class Signup extends AppCompatActivity {
    Button submit_user;
    ImageButton selectdob;
    EditText email, password, cfmPassword, name, nric, dob, mobile, others;
    TextView school, diet, tnu, pp, pwdErrorMsg, emailErrorMsg, nricErrorMessage, schoolErrorMessage, dietErrorMessage;
    Links link;
    AppCompatCheckBox checkBox;
    LinearLayout progressbar;
    ConstraintLayout ui;

    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    private int mYear, mMonth, mDay;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> finish());
        Intent getIntent = getIntent();
        submit_user = findViewById(R.id.submit_user);
        selectdob = findViewById(R.id.selectdob);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        others = findViewById(R.id.others);
        others.setVisibility(View.GONE);
        cfmPassword = findViewById(R.id.cfmPassword);
        name = findViewById(R.id.name);
        nric = findViewById(R.id.nric);
        dob = findViewById(R.id.dob);
        school = findViewById(R.id.school);
        mobile = findViewById(R.id.mobile);
        diet = findViewById(R.id.diet);
        tnu = findViewById(R.id.tnu);
        tnu.setOnClickListener(v -> tncDialogue());
        pp = findViewById(R.id.pp);
        pp.setOnClickListener(v -> ppDialogue());
        checkBox = findViewById(R.id.checkbox);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        ui = findViewById(R.id.ui);

        String getMethod = getIntent.getStringExtra("method");
        switch (getMethod) {
            case "facebook":
                name.setText(getIntent.getStringExtra("name"));
                email.setText(getIntent.getStringExtra("email"));
                password.setVisibility(View.GONE);
                password.setText("Facebook");
                cfmPassword.setVisibility(View.GONE);
                cfmPassword.setText("Facebook");
                email.setVisibility(View.GONE);

                break;
            case "google":
                name.setText(getIntent.getStringExtra("name"));
                email.setText(getIntent.getStringExtra("email"));
                password.setVisibility(View.GONE);
                password.setText("Google");
                cfmPassword.setVisibility(View.GONE);
                cfmPassword.setText("Google");
                email.setVisibility(View.GONE);
                break;
            default:

                break;
        }

        //Error Messages
        pwdErrorMsg = findViewById(R.id.pwdErrorMessage);
        pwdErrorMsg.setVisibility(View.GONE);
        emailErrorMsg = findViewById(R.id.emailErrorMessage);
        emailErrorMsg.setVisibility(View.GONE);
        nricErrorMessage = findViewById(R.id.nricErrorMessage);
        nricErrorMessage.setVisibility(View.GONE);
        schoolErrorMessage = findViewById(R.id.schoolErrorMessage);
        schoolErrorMessage.setVisibility(View.GONE);
        dietErrorMessage = findViewById(R.id.dietErrorMessage);
        dietErrorMessage.setVisibility(View.GONE);
        selectdob = findViewById(R.id.selectdob);

        dob.setOnClickListener(v -> selectDate());
        selectdob.setOnClickListener(v -> selectDate());
//
//        btnScan = findViewById(R.id.btnScan);
//        btnScan.setOnClickListener(v -> {
//            Intent intent = new Intent(this, OcrCaptureActivity.class);
//            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
//            intent.putExtra(OcrCaptureActivity.UseFlash, false);
//
//            startActivityForResult(intent, RC_OCR_CAPTURE);
//        });

        school.setOnClickListener(v -> selectSchool());
        diet.setOnClickListener(v -> selectDiet());

        submit_user.setOnClickListener((View v) -> {

            progressbar.setVisibility(View.VISIBLE);
            if (!checkEmpty()) {

                progressbar.setVisibility(View.GONE);
                Toast.makeText(this, "Please Fill in all fields!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("CHECK VALIDATION: ", checkValidation().toString());
                if (checkValidation()) {
                    if (checkBox.isChecked()) {
                        if (checkNetwork()) {
                            Links link = new Links();
                            Register register = new Register();
                            register.execute(link.getRegister());
                        } else {

                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(this, "Please switch on your data or wifi", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(this, "Please Read and Accept our Terms of Use and Privacy Policy", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(this, "There are still some errors", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    public Boolean validate_nric(String nric) {
        String nricUpper = nric.toUpperCase();
        return nricUpper.matches("\\p{Upper}\\d{7}\\p{Upper}");
    }

    public Boolean checkSchool(String school) {
        return school.equals("School");
    }

    public Boolean checkDiet(String diet) {
        return diet.equals("Dietary Requirements");
    }

    public Boolean validate_email(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public Boolean checkPassword(String password, String cfmPassword) {
        return password.equals(cfmPassword);
    }

    public boolean checkNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert conMgr != null;
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            // notify user you are online
            return true;

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            progressbar.setVisibility(View.GONE);

            // notify user you are not online
            Toast.makeText(this, "Please Switch your data on", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    public void tncDialogue() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this);
        dialog.setCancelable(false);
        dialog.setTitle("Terms of use");
        dialog.setMessage("Terms of use here");
        dialog.setPositiveButton("OK", (dialogInterface, i) -> {
        });
        AlertDialog dialogue = dialog.create();
        dialogue.show();
    }

    public void ppDialogue() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this);
        dialog.setCancelable(false);
        dialog.setTitle("Privacy Policy");
        dialog.setMessage("privacy policy here");
        dialog.setPositiveButton("OK", (dialogInterface, i) -> {
        });
        AlertDialog dialogue = dialog.create();
        dialogue.show();
    }

    private class Register extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String[] splitDate = dob.getText().toString().split("-");
            String newDate = splitDate[2] + splitDate[1] + splitDate[0];
            Log.e("NEW DATE: ", newDate);


            HttpRequest request = new HttpRequest();
            link = new Links();
            return request.PostRequest(link.getRegister()
                    , "name="
                            + name.getText().toString()
                            + "&email="
                            + email.getText().toString()
                            + "&nric="
                            + nric.getText().toString()
                            + "&dob="
                            + dob.getText().toString().split("-")[2] + "-"
                            + dob.getText().toString().split("-")[1] + "-"
                            + dob.getText().toString().split("-")[0]
                            + "&mobile="
                            + mobile.getText().toString()
                            + "&school="
                            + school.getText().toString()
                            + "&diet="
                            + diet.getText().toString()
                            + "&password="
                            + password.getText().toString()
                            + "&statuses_id=" + String.valueOf(getRegistrationStatus(dob.getText().toString())));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressbar.setVisibility(View.GONE);
            Log.e("JSON RETURN: ", s);
            String[] splitString = s.split("");
            Log.e("ITEM RESULT", splitString[2]);
            if (splitString[2].equals("D")) {
                Toast.makeText(Signup.this, "You have already Registered!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject result = new JSONObject(s);
                    int status = result.getInt("status");
                    String message = result.getString("message");

                    Log.i("JSON MESSAGE:", message);
                    Log.i("JSON STATUS: ", String.valueOf(status));


                    if (status == 200) {
                        int type = result.getInt("type");
                        String display_message = result.getString("display_message");
                        String display_title = result.getString("display_title");
                        Log.i("JSON DISPLAY MESSAGE", display_message);

                        switch (type) {
                            case 1: {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this);
                                dialog.setCancelable(false);
                                dialog.setTitle(display_title);

                                dialog.setMessage(display_message);
                                dialog.setPositiveButton("OK", (dialogInterface, i) -> finish());
                                AlertDialog dialogue = dialog.create();

                                dialogue.show();
                                break;
                            }
                            case 2: {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this);
                                dialog.setCancelable(false);
                                dialog.setTitle(display_title);

                                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sessionManager.edit();
                                editor.putString(SESSION_ID, "200");
                                editor.putString("email", email.getText().toString());
                                editor.putString("name", name.getText().toString());
                                editor.putString("number", mobile.getText().toString());
                                editor.putString("dob", dob.getText().toString());
                                editor.apply();
                                dialog.setMessage(display_message);
                                dialog.setPositiveButton("OK", (dialogInterface, i) -> startActivity(new Intent(Signup.this, Home.class)));
                                AlertDialog dialogue = dialog.create();
                                dialogue.show();
                                break;
                            }
                            case 3: {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this);
                                dialog.setCancelable(false);
                                dialog.setTitle(display_title);

                                dialog.setMessage(display_message);
                                dialog.setPositiveButton("OK", (dialogInterface, i) -> finish());
                                AlertDialog dialogue = dialog.create();

                                dialogue.show();
                                break;
                            }
                        }
//                    startActivity(new Intent(Signup.this, Home.class));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Boolean checkValidation() {
        boolean[] checkallvalidation = new boolean[5];
        if (!validate_email(email.getText().toString())) {
            emailErrorMsg.setVisibility(View.VISIBLE);
            emailErrorMsg.setText("Your Email is invalid");
            checkallvalidation[0] = false;

        } else {
            emailErrorMsg.setVisibility(View.GONE);
            checkallvalidation[0] = true;
        }
        if (!validate_nric(nric.getText().toString())) {

            checkallvalidation[1] = true;

        } else {
            checkallvalidation[1] = true;
        }
        if (!checkPassword(password.getText().toString(), cfmPassword.getText().toString())) {
            pwdErrorMsg.setVisibility(View.VISIBLE);
            pwdErrorMsg.setText("Your passwords do not match!");
            checkallvalidation[2] = false;

        } else {
            pwdErrorMsg.setVisibility(View.GONE);
            checkallvalidation[2] = true;
        }
        if (checkSchool(school.getText().toString())) {
            schoolErrorMessage.setVisibility(View.VISIBLE);
            schoolErrorMessage.setText("Please Select a School");
            checkallvalidation[3] = false;

        } else {
            schoolErrorMessage.setVisibility(View.GONE);
            if (school.getText().toString() == "OTHERS") {

            }
            checkallvalidation[3] = true;
        }
        if (checkDiet(diet.getText().toString())) {
            dietErrorMessage.setVisibility(View.VISIBLE);
            dietErrorMessage.setText("Please select one");
            checkallvalidation[4] = false;

        } else {
            dietErrorMessage.setVisibility(View.GONE);
            checkallvalidation[4] = true;
        }
        ArrayList<Boolean> falseItems = new ArrayList<>();
        Log.e("VALIDATED LENGTH: ", checkallvalidation.length + "");
        for (int i = 0; i < checkallvalidation.length; i++) {
            Log.e("VALIDATED " + i + ":", String.valueOf(checkallvalidation[i]));
            if (!checkallvalidation[i]) {
                falseItems.add(checkallvalidation[i]);
            }
        }
        if (falseItems.size() >= 1) {
            return false;
        } else {
            return true;
        }


    }

    public Boolean checkEmpty() {
        return !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !cfmPassword.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !nric.getText().toString().isEmpty() && !dob.getText().toString().isEmpty();
    }

    public void selectDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void selectSchool() {
        final CharSequence[] items = {"ADMIRALTY SECONDARY SCHOOL"
                , "AHMAD IBRAHIM SECONDARY SCHOOL"
                , "ANDERSON SECONDARY SCHOOL"
                , "ANG MO KIO SECONDARY SCHOOL"
                , "ANGLICAN HIGH SCHOOL"
                , "ANGLO-CHINESE SCHOOL (BARKER ROAD)"
                , "ANGLO-CHINESE SCHOOL (INDEPENDENT)"
                , "ASSUMPTION ENGLISH SCHOOL"
                , "BARTLEY SECONDARY SCHOOL"
                , "BEATTY SECONDARY SCHOOL"
                , "BEDOK GREEN SECONDARY SCHOOL"
                , "BEDOK NORTH SECONDARY SCHOOL"
                , "BEDOK SOUTH SECONDARY SCHOOL"
                , "BEDOK VIEW SECONDARY SCHOOL"
                , "BENDEMEER SECONDARY SCHOOL"
                , "BISHAN PARK SECONDARY SCHOOL"
                , "BOON LAY SECONDARY SCHOOL"
                , "BOWEN SECONDARY SCHOOL"
                , "BROADRICK SECONDARY SCHOOL"
                , "BUKIT BATOK SECONDARY SCHOOL"
                , "BUKIT MERAH SECONDARY SCHOOL"
                , "BUKIT PANJANG GOVT. HIGH SCHOOL"
                , "BUKIT VIEW SECONDARY SCHOOL"
                , "CANBERRA SECONDARY SCHOOL"
                , "CATHOLIC HIGH SCHOOL"
                , "CEDAR GIRLS' SECONDARY SCHOOL"
                , "CHANGKAT CHANGI SECONDARY SCHOOL"
                , "CHIJ KATONG CONVENT"
                , "CHIJ SECONDARY (TOA PAYOH)"
                , "CHIJ ST. JOSEPH'S CONVENT"
                , "CHIJ ST. NICHOLAS GIRLS' SCHOOL"
                , "CHIJ ST. THERESA'S CONVENT"
                , "CHONG BOON SECONDARY SCHOOL"
                , "CHRIST CHURCH SECONDARY SCHOOL"
                , "CHUA CHU KANG SECONDARY SCHOOL"
                , "CHUNG CHENG HIGH SCHOOL (MAIN)"
                , "CHUNG CHENG HIGH SCHOOL (YISHUN)"
                , "CLEMENTI TOWN SECONDARY SCHOOL"
                , "COMMONWEALTH SECONDARY SCHOOL"
                , "COMPASSVALE SECONDARY SCHOOL"
                , "CRESCENT GIRLS' SCHOOL"
                , "DAMAI SECONDARY SCHOOL"
                , "DEYI SECONDARY SCHOOL"
                , "DUNEARN SECONDARY SCHOOL"
                , "DUNMAN HIGH SCHOOL"
                , "DUNMAN SECONDARY SCHOOL"
                , "EAST SPRING SECONDARY SCHOOL"
                , "EAST VIEW SECONDARY SCHOOL"
                , "EDGEFIELD SECONDARY SCHOOL"
                , "EVERGREEN SECONDARY SCHOOL"
                , "FAIRFIELD METHODIST SCHOOL (SECONDARY)"
                , "FAJAR SECONDARY SCHOOL"
                , "FUCHUN SECONDARY SCHOOL"
                , "FUHUA SECONDARY SCHOOL"
                , "GAN ENG SENG SCHOOL"
                , "GEYLANG METHODIST SCHOOL (SECONDARY)"
                , "GREENDALE SECONDARY SCHOOL"
                , "GREENRIDGE SECONDARY SCHOOL"
                , "GREENVIEW SECONDARY SCHOOL"
                , "GUANGYANG SECONDARY SCHOOL"
                , "HAI SING CATHOLIC SCHOOL"
                , "HILLGROVE SECONDARY SCHOOL"
                , "HOLY INNOCENTS' HIGH SCHOOL"
                , "HONG KAH SECONDARY SCHOOL"
                , "HOUGANG SECONDARY SCHOOL"
                , "HUA YI SECONDARY SCHOOL"
                , "HWA CHONG INSTITUTION"
                , "JUNYUAN SECONDARY SCHOOL"
                , "JURONG SECONDARY SCHOOL"
                , "JURONG WEST SECONDARY SCHOOL"
                , "JURONGVILLE SECONDARY SCHOOL"
                , "JUYING SECONDARY SCHOOL"
                , "KENT RIDGE SECONDARY SCHOOL"
                , "KRANJI SECONDARY SCHOOL"
                , "KUO CHUAN PRESBYTERIAN SECONDARY SCHOOL"
                , "LOYANG SECONDARY SCHOOL"
                , "MANJUSRI SECONDARY SCHOOL"
                , "MARIS STELLA HIGH SCHOOL"
                , "MARSILING SECONDARY SCHOOL"
                , "MAYFLOWER SECONDARY SCHOOL"
                , "METHODIST GIRLS' SCHOOL (SECONDARY)"
                , "MONTFORT SECONDARY SCHOOL"
                , "NAN CHIAU HIGH SCHOOL"
                , "NAN HUA HIGH SCHOOL"
                , "NANYANG GIRLS' HIGH SCHOOL"
                , "NAVAL BASE SECONDARY SCHOOL"
                , "NEW TOWN SECONDARY SCHOOL"
                , "NGEE ANN SECONDARY SCHOOL"
                , "NORTH VISTA SECONDARY SCHOOL"
                , "NORTHBROOKS SECONDARY SCHOOL"
                , "NORTHLAND SECONDARY SCHOOL"
                , "NUS HIGH SCHOOL OF MATHEMATICS AND SCIENCE"
                , "ORCHID PARK SECONDARY SCHOOL"
                , "OUTRAM SECONDARY SCHOOL"
                , "PASIR RIS CREST SECONDARY SCHOOL"
                , "PASIR RIS SECONDARY SCHOOL"
                , "PAYA LEBAR METHODIST GIRLS' SCHOOL (SECONDARY)"
                , "PEI HWA SECONDARY SCHOOL"
                , "PEICAI SECONDARY SCHOOL"
                , "PEIRCE SECONDARY SCHOOL"
                , "PING YI SECONDARY SCHOOL"
                , "PRESBYTERIAN HIGH SCHOOL"
                , "PUNGGOL SECONDARY SCHOOL"
                , "QUEENSTOWN SECONDARY SCHOOL"
                , "QUEENSWAY SECONDARY SCHOOL"
                , "RAFFLES GIRLS' SCHOOL (SECONDARY)"
                , "RAFFLES INSTITUTION"
                , "REGENT SECONDARY SCHOOL"
                , "RIVER VALLEY HIGH SCHOOL"
                , "RIVERSIDE SECONDARY SCHOOL"
                , "SCHOOL OF SCIENCE AND TECHNOLOGY, SINGAPORE"
                , "SCHOOL OF THE ARTS, SINGAPORE"
                , "SEMBAWANG SECONDARY SCHOOL"
                , "SENG KANG SECONDARY SCHOOL"
                , "SERANGOON GARDEN SECONDARY SCHOOL"
                , "SERANGOON SECONDARY SCHOOL"
                , "SHUQUN SECONDARY SCHOOL"
                , "SINGAPORE CHINESE GIRLS' SCHOOL"
                , "SINGAPORE SPORTS SCHOOL"
                , "SPRINGFIELD SECONDARY SCHOOL"
                , "ST. ANDREW'S SECONDARY SCHOOL"
                , "ST. ANTHONY'S CANOSSIAN SECONDARY SCHOOL"
                , "ST. GABRIEL'S SECONDARY SCHOOL"
                , "ST. HILDA'S SECONDARY SCHOOL"
                , "ST. JOSEPH'S INSTITUTION"
                , "ST. MARGARET'S SECONDARY SCHOOL"
                , "ST. PATRICK'S SCHOOL"
                , "SWISS COTTAGE SECONDARY SCHOOL"
                , "TAMPINES SECONDARY SCHOOL"
                , "TANGLIN SECONDARY SCHOOL"
                , "TANJONG KATONG GIRLS' SCHOOL"
                , "TANJONG KATONG SECONDARY SCHOOL"
                , "TECK WHYE SECONDARY SCHOOL"
                , "TEMASEK SECONDARY SCHOOL"
                , "UNITY SECONDARY SCHOOL"
                , "VICTORIA SCHOOL"
                , "WEST SPRING SECONDARY SCHOOL"
                , "WESTWOOD SECONDARY SCHOOL"
                , "WHITLEY SECONDARY SCHOOL"
                , "WOODGROVE SECONDARY SCHOOL"
                , "WOODLANDS RING SECONDARY SCHOOL"
                , "WOODLANDS SECONDARY SCHOOL"
                , "XINMIN SECONDARY SCHOOL"
                , "YIO CHU KANG SECONDARY SCHOOL"
                , "YISHUN SECONDARY SCHOOL"
                , "YISHUN TOWN SECONDARY SCHOOL"
                , "YUAN CHING SECONDARY SCHOOL"
                , "YUHUA SECONDARY SCHOOL"
                , "YUSOF ISHAK SECONDARY SCHOOL"
                , "YUYING SECONDARY SCHOOL"
                , "ZHENGHUA SECONDARY SCHOOL"
                , "ZHONGHUA SECONDARY SCHOOL"
                , "MERIDIAN SECONDARY SCHOOL"
                , "OTHERS"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            // Do something with the selection
            if (items[item] == "OTHERS") {
                others.setVisibility(View.VISIBLE);
                school.setText(items[item]);
            } else {
                others.setVisibility(View.GONE);
                school.setText(items[item]);
            }

        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void selectDiet() {
        final CharSequence[] items = {"VEGETARIAN", "NONE"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            // Do something with the selection

            diet.setText(items[item]);


        });
        AlertDialog alert = builder.create();
        alert.show();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RC_OCR_CAPTURE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
//                    ocrText = text;
//                    Log.e(TAG, "onActivityResult: " + ocrText);
//                    Log.e(TAG, "Text read: " + text);
//                    nric.setText(text);
//                } else {
//                    Log.d(TAG, "No Text captured, intent data is null");
//                }
//            } else {
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    public int getRegistrationStatus(String date) {
        String[] yearString = date.split("-");
        int year = Integer.parseInt(yearString[2]);
        if (year <= 2000) {
            return 1;
        } else if (year >= 2003) {
            return 3;
        } else {
            return 2;
        }
    }
}
