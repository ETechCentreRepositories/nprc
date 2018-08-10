package ngeeann.com.redcamp.NavigationItems;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.R;

public class TodayPoll extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button btnChoice1, btnChoice2, btnChoice3, btnVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_poll);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);
        btnVote = findViewById(R.id.btnVote);
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = findViewById(v.getId());
        btnVote.setClickable(true);
        btnVote.setBackgroundResource(R.drawable.poll_rounded_vote_btn_active);
        switch (v.getId()) {
            case R.id.btnChoice1:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                break;

            case R.id.btnChoice2:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                break;

            case R.id.btnChoice3:
                btnChoice1.setBackgroundResource(R.drawable.poll_rounded_btn_chosen);
                btnChoice2.setBackgroundResource(R.drawable.poll_rounded_btn);
                btnChoice3.setBackgroundResource(R.drawable.poll_rounded_btn);
                break;

            case R.id.btnVote:
                AlertDialog.Builder dialog = new AlertDialog.Builder(TodayPoll.this);
                LayoutInflater li = LayoutInflater.from(TodayPoll.this);
                final View gtnc = li.inflate(R.layout.dialog_complete_poll ,null);
                dialog.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent moveBack = new Intent(TodayPoll.this, Home.class);
                        startActivity(moveBack);
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.setView(gtnc);
                AlertDialog dialogue = dialog.create();
                dialogue.show();


            default:
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
}
