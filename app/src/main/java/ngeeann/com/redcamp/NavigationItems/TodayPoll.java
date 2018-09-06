package ngeeann.com.redcamp.NavigationItems;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.SQLiteQuestions.DatabaseHelper;

public class TodayPoll extends AppCompatActivity{
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;

    CardView cvPoll;
//    CardView cvResult;
    TextView tvQuestion,tvDetail;
    Toolbar toolbar;
    Button btnChoice1, btnChoice2, btnChoice3, btnChoice4, btnChoice5, btnVote;
    int questionNumber = 0;
    String chosenAnswer = null;
    String assignedQuestionID = "";
    //JSONArray allQuestionsArray;
    JSONObject allQuestionsObject;
    ArrayList<Integer> buttonIDs = new ArrayList<>();
    ArrayList<Integer> progressbardetailIDs = new ArrayList<>();
    List<String> assignedQuestions;

    ProgressBar progressBar1,progressBar2,progressBar3,progressBar4,progressBar5;
    TextView progressBarDetail1,progressBarDetail2,progressBarDetail3,progressBarDetail4,progressBarDetail5,tvResultsQuestion,tvResultsDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_poll);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TodayPoll.this);
                LayoutInflater li = LayoutInflater.from(TodayPoll.this);
                final View gtnc = li.inflate(R.layout.dialog_poll_exit_warning ,null);
                dialog.setPositiveButton("I will stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent moveBack = new Intent(TodayPoll.this, Home.class);
                        startActivity(moveBack);
                        finish();
                    }
                });
                dialog.setCancelable(true);
                dialog.setView(gtnc);
                AlertDialog dialogue = dialog.create();
                dialogue.show();
            }
        });

        tvQuestion = findViewById(R.id.tvQuestion);
        tvDetail = findViewById(R.id.tvDetail);
        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);
        btnChoice4 = findViewById(R.id.btnChoice4);
        btnChoice5 = findViewById(R.id.btnChoice5);
        buttonIDs.add(R.id.btnChoice1);
        buttonIDs.add(R.id.btnChoice2);
        buttonIDs.add(R.id.btnChoice3);
        buttonIDs.add(R.id.btnChoice4);
        buttonIDs.add(R.id.btnChoice5);
        btnVote = findViewById(R.id.btnVote);

        cvPoll = findViewById(R.id.cvPoll);
//        cvResult = findViewById(R.id.cvResult);

        tvResultsDetail = findViewById(R.id.tvResultsDetail);
        tvResultsQuestion = findViewById(R.id.tvResultsQuestion);
        progressBar1 = findViewById(R.id.progressbar1);
        progressBar2 = findViewById(R.id.progressbar2);
        progressBar3 = findViewById(R.id.progressbar3);
        progressBar4 = findViewById(R.id.progressbar4);
        progressBar5 = findViewById(R.id.progressbar5);
        progressBarDetail1 = findViewById(R.id.progressbardetail1);
        progressBarDetail2 = findViewById(R.id.progressbardetail2);
        progressBarDetail3 = findViewById(R.id.progressbardetail3);
        progressBarDetail4 = findViewById(R.id.progressbardetail4);
        progressBarDetail5 = findViewById(R.id.progressbardetail5);
        progressbardetailIDs.add(R.id.progressbardetail1);
        progressbardetailIDs.add(R.id.progressbardetail2);
        progressbardetailIDs.add(R.id.progressbardetail3);
        progressbardetailIDs.add(R.id.progressbardetail4);
        progressbardetailIDs.add(R.id.progressbardetail5);

//        btnNextPoll = findViewById(R.id.btnResultsNext);

        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);

        //get assigned questions through tribe and day(1,2 or 3)
//        assignedQuestions = getAssignedQuestionsByTribeDay("apaches",1);
//        assignedQuestions = getAssignedQuestionsByTribeDay("apaches",2);
//        assignedQuestions = getAssignedQuestionsByTribeDay("apaches",3);
//        assignedQuestions = getAssignedQuestionsByTribeDay("vikings",1);
//        assignedQuestions = getAssignedQuestionsByTribeDay("vikings",2);
//        assignedQuestions = getAssignedQuestionsByTribeDay("vikings",3);
//        assignedQuestions = getAssignedQuestionsByTribeDay("centurions",1);
//        assignedQuestions = getAssignedQuestionsByTribeDay("centurions",2);
//        assignedQuestions = getAssignedQuestionsByTribeDay("centurions",3);
//        assignedQuestions = getAssignedQuestionsByTribeDay("ninjas",1);
//        assignedQuestions = getAssignedQuestionsByTribeDay("ninjas",2);
//        assignedQuestions = getAssignedQuestionsByTribeDay("ninjas",3);
//        assignedQuestions = getAssignedQuestionsByTribeDay("spartans",1);
//        assignedQuestions = getAssignedQuestionsByTribeDay("spartans",2);
        assignedQuestions = getAssignedQuestionsByTribeDay("spartans",3);

        String stringGetQuestion = sessionManager.getString("allQuestions","emptydb");
        if(!stringGetQuestion.equalsIgnoreCase("emptydb")){
            try{
                 allQuestionsObject = new JSONObject(stringGetQuestion);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        setQuestion();
    }

    public void onPollButtonClick(View v) {
        Button clickedButton = findViewById(v.getId());
        btnVote.setClickable(true);
        btnVote.setBackgroundResource(R.drawable.poll_rounded_vote_btn_active);
        switch (clickedButton.getId()) {
            case R.id.btnChoice1:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice4.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice5.setBackgroundResource(R.drawable.poll_rounded_btn);
                chosenAnswer = btnChoice1.getText().toString();
                break;

            case R.id.btnChoice2:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice4.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice5.setBackgroundResource(R.drawable.poll_rounded_btn);
                chosenAnswer = btnChoice2.getText().toString();
                break;

            case R.id.btnChoice3:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice4.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice5.setBackgroundResource(R.drawable.poll_rounded_btn);
                chosenAnswer = btnChoice3.getText().toString();
                break;

            case R.id.btnChoice4:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice4.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice5.setBackgroundResource(R.drawable.poll_rounded_btn);
                chosenAnswer = btnChoice4.getText().toString();
                break;

            case R.id.btnChoice5:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice4.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice5.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                chosenAnswer = btnChoice5.getText().toString();
                break;

            case R.id.btnVote:
                //set to false to prevent multiple submissions
                btnVote.setClickable(false);

                submitAnswer(chosenAnswer);
//                showPollResults(assignedQuestionID);
                questionNumber++;
                Log.i("todaypoll",questionNumber + " " +assignedQuestionID.toString());
                if(questionNumber >= assignedQuestions.size()){
                    //No more assigned questions
                    //check for internet connection
                    //upload answers to online db
                    //(answers must be RETRIEVED from localdb in case sharedpreferences was cleared)
                    String stringGetQuestion = sessionManager.getString("allQuestions","emptydb");
                    if(!stringGetQuestion.equalsIgnoreCase("emptydb")){
                        try{
                            allQuestionsObject = new JSONObject(stringGetQuestion);
                            //Log.i("allQuestions string",allQuestionsObject.toString());
                            d("allQuestions",allQuestionsObject.toString());
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    AlertDialog.Builder dialog = new AlertDialog.Builder(TodayPoll.this);
                    LayoutInflater li = LayoutInflater.from(TodayPoll.this);
                    final View gtnc = li.inflate(R.layout.dialog_complete_poll ,null);
                    dialog.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent moveBack = new Intent(TodayPoll.this, Home.class);
                            startActivity(moveBack);
                            finish();
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.setView(gtnc);
                    AlertDialog dialogue = dialog.create();
                    dialogue.show();
                } else {
                    setQuestion();
//                    cvResult.setVisibility(View.GONE);
                    cvPoll.setVisibility(View.VISIBLE);
                }
//                cvResult.setVisibility(View.VISIBLE);
                //Toast.makeText(TodayPoll.this,chosenAnswer,Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnResultsNext:
                //set to false to prevent multiple calls which will affect question number
//                btnNextPoll.setClickable(false);


                //Toast.makeText(TodayPoll.this,"button clickednext",Toast.LENGTH_SHORT).show();
                break;

            default:
                //Toast.makeText(TodayPoll.this,"button clickeddefault",Toast.LENGTH_SHORT).show();
                break;
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

    //called when moving on to the next question/poll
    private void resetPollButtons(){
        btnVote.setClickable(false);
        btnVote.setBackgroundResource(R.drawable.poll_rounded_vote_btn);
        btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn);
        btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
        btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
        btnChoice4.setBackgroundResource(R.drawable.poll_rounded_btn);
        btnChoice5.setBackgroundResource(R.drawable.poll_rounded_btn);
        chosenAnswer = null;
    }

//    private void showPollResults(String tribe){
//        //set visibility of poll results layout
//        cvPoll.setVisibility(View.GONE);
////        cvResult.setVisibility(View.VISIBLE);
//        btnNextPoll.setClickable(true);
//
//
//    }

    private void setQuestion(){
        resetPollButtons();
        try{
            Log.i("todaypoll",questionNumber + " " + assignedQuestions);
           String assignedQ = assignedQuestions.get(questionNumber);
            JSONObject question = allQuestionsObject.getJSONObject(assignedQ);

            tvQuestion.setText(question.getString("question"));
            tvResultsQuestion.setText(question.getString("question"));

            //tvResultsDetail.setText("305 people have participated in this poll");
            //tvDetail.setText("305 people have participated in this poll");

            JSONArray options = question.getJSONArray("options");
            assignedQuestionID = question.getString("tribe");
            for(int i = 0; i < options.length(); i++){
                String optionString = options.getString(i);
                Button btnOption = findViewById(buttonIDs.get(i));
                TextView progressbardetail = findViewById(progressbardetailIDs.get(i));
                if(optionString.equalsIgnoreCase("")){
                    btnOption.setVisibility(View.GONE);
                } else {
                    btnOption.setVisibility(View.VISIBLE);
                    btnOption.setText(options.getString(i));
                    progressbardetail.setText(options.getString(i));
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private boolean submitAnswer(String answer){
        //update userAnswer sharedpreference and LOCAL db
        try {
            JSONObject question = allQuestionsObject.getJSONObject(assignedQuestionID);
            question.put("userAnswer",answer);
            allQuestionsObject.put(assignedQuestionID,question);

            //update sharedpreferences
            sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sessionManager.edit();
            editor.putString("allQuestions", allQuestionsObject.toString());
            editor.commit();

            //update localdb
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.updateUserAnswer(question.getString("tribe"),answer);


        } catch (JSONException e){
            e.printStackTrace();
        }


        return false;
    };

    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d("allQuestions", message.substring(start, end));
        }
    }

    //Not all questions has been assigned
    //done: apaches
    private List<String> getAssignedQuestionsByTribeDay(String tribe, int day){
        ArrayList<String> questionList = new ArrayList<>();
        if(tribe.equalsIgnoreCase("apaches")){
            List<List<String>> apaches = new ArrayList<>();
            apaches.add(Arrays.asList("1a","hs1","hs2","lsct1","lsct2","ba1","ba2","soe1","soe2","1b"));
            apaches.add(Arrays.asList("ccas1","ccas2","hms1","hms2","fms1","fms2","2a","2b"));
            apaches.add(Arrays.asList("de1","de2","ict1","ict2","3a","3b","3c","3d","3e","3f"));

            return apaches.get(day-1);
        }

        if(tribe.equalsIgnoreCase("vikings")){
            List<List<String>> apaches = new ArrayList<>();
            apaches.add(Arrays.asList("1a","ba1","ba2","soe1","soe2","fms1","fms2","hms1","hms","1b"));
            apaches.add(Arrays.asList("de1","de2","ict1","ict2","ccas1","ccas2","2a","2b"));
            apaches.add(Arrays.asList("hs1","hs2","lsct1","lsct2","3a","3b","3c","3d","3e","3f"));
            return apaches.get(day-1);
        }

        if(tribe.equalsIgnoreCase("centurions")){
            List<List<String>> apaches = new ArrayList<>();
            apaches.add(Arrays.asList("1a","de1","de2","ict1","ict2","ccas1","ccas2","1b"));
            apaches.add(Arrays.asList("fms1","fms2","hms1","hms2","hs1","hs2","lsct1","lsct2","2a","2b"));
            apaches.add(Arrays.asList("ba1","ba2","soe1","soe2","3a","3b","3c","3d","3e","3f"));

            return apaches.get(day-1);
        }

        if(tribe.equalsIgnoreCase("ninjas")){
            List<List<String>> apaches = new ArrayList<>();
            apaches.add(Arrays.asList("1a","hms1","hms2","fms1","fms2","hs1","hs2","lsct1","lsct2","1b"));
            apaches.add(Arrays.asList("ba1","ba2","soe1","soe2","de1","de2","ict1","ict2","2a","2b"));
            apaches.add(Arrays.asList("ccas1","ccas2","3a","3b","3c","3d","3e","3f"));

            return apaches.get(day-1);
        }

        if(tribe.equalsIgnoreCase("spartans")){
            List<List<String>> apaches = new ArrayList<>();
            apaches.add(Arrays.asList("1a","ccas1","ccas2","de1","de2","ict1","ict2","1b"));
            apaches.add(Arrays.asList("hs1","hs2","lsct1","lsct2","ba1","ba2","soe1","soe2","2a","2b"));
            apaches.add(Arrays.asList("fms1","fms2","hms1","hms2","3a","3b","3c","3d","3e","3f"));

            return apaches.get(day-1);
        }

        return null;

    }


}
