package ngeeann.com.redcamp.Content;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ngeeann.com.redcamp.R;

public class CampProgramme extends AppCompatActivity {
    Button d1,d2,d3;
    TextView content;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_programme);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
        });
        d1 = findViewById(R.id.day1);
        d2 = findViewById(R.id.day2);
        d3 = findViewById(R.id.day3);
        content = findViewById(R.id.content);
        ArrayList<String> contents = new ArrayList<>();

        contents.add("<h1 style='font-size:10px'><u>20 November, Tuesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 2" +
                "<br><b>05:30pm</b> &nbsp&nbsp Course & Admissions Talk" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 1");

        contents.add("<h1 style='font-size:10px'><u>21 November, Wednesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Skit Performance" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 3" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 4" +
                "<br><b>05:30pm</b> &nbsp&nbsp CCA Fiesta" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 2");

        contents.add("<h1 style='font-size:15px'><u>22 November, Thursday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 5" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Camp Finale" +
                "<br><b>06:00pm</b> &nbsp&nbsp Dinner" +
                "<br><b>07:30pm</b> &nbsp&nbsp End of Red Camp");

        //init
        setd1();
        d1.setOnClickListener(v->setd1());
        d2.setOnClickListener(v->setd2());
        d3.setOnClickListener(v->setd3());




    }
    public void setd1(){
        ArrayList<String> contents = new ArrayList<>();

        contents.add("<h1 style='font-size:10px'><u>20 November, Tuesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 2" +
                "<br><b>05:30pm</b> &nbsp&nbsp Course & Admissions Talk" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 1");

        contents.add("<h1 style='font-size:10px'><u>21 November, Wednesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Skit Performance" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 3" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 4" +
                "<br><b>05:30pm</b> &nbsp&nbsp CCA Fiesta" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 2");

        contents.add("<h1 style='font-size:15px'><u>22 November, Thursday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 5" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Camp Finale" +
                "<br><b>06:00pm</b> &nbsp&nbsp Dinner" +
                "<br><b>07:30pm</b> &nbsp&nbsp End of Red Camp");
        d2.setBackgroundColor(getResources().getColor(R.color.grey));
        d3.setBackgroundColor(getResources().getColor(R.color.grey));
        d1.setBackgroundColor(getResources().getColor(android.R.color.white));
        content.setText(Html.fromHtml(contents.get(0)));
    }
    public void setd2(){
        ArrayList<String> contents = new ArrayList<>();

        contents.add("<h1 style='font-size:10px'><u>20 November, Tuesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 2" +
                "<br><b>05:30pm</b> &nbsp&nbsp Course & Admissions Talk" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 1");

        contents.add("<h1 style='font-size:10px'><u>21 November, Wednesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Skit Performance" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 3" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 4" +
                "<br><b>05:30pm</b> &nbsp&nbsp CCA Fiesta" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 2");

        contents.add("<h1 style='font-size:15px'><u>22 November, Thursday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 5" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Camp Finale" +
                "<br><b>06:00pm</b> &nbsp&nbsp Dinner" +
                "<br><b>07:30pm</b> &nbsp&nbsp End of Red Camp");
        d1.setBackgroundColor(getResources().getColor(R.color.grey));
        d3.setBackgroundColor(getResources().getColor(R.color.grey));
        d2.setBackgroundColor(getResources().getColor(android.R.color.white));
        content.setText(Html.fromHtml(contents.get(1)));
    }
    public void setd3(){
        ArrayList<String> contents = new ArrayList<>();

        contents.add("<h1 style='font-size:10px'><u>20 November, Tuesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 2" +
                "<br><b>05:30pm</b> &nbsp&nbsp Course & Admissions Talk" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 1");

        contents.add("<h1 style='font-size:10px'><u>21 November, Wednesday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Skit Performance" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 3" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Path of Discovery 4" +
                "<br><b>05:30pm</b> &nbsp&nbsp CCA Fiesta" +
                "<br><b>06:00pm</b> &nbsp&nbsp Mass Dance" +
                "<br><b>06:30pm</b> &nbsp&nbsp End of Day 2");

        contents.add("<h1 style='font-size:15px'><u>22 November, Thursday</u></h1>" +
                "<b>08:30am</b> &nbsp&nbsp Registration:" +
                "<br><b>09:00am</b> &nbsp&nbsp Event Opening" +
                "<br><b>10:00am</b> &nbsp&nbsp Path of Discovery 5" +
                "<br><b>01:00pm</b> &nbsp&nbsp Lunch" +
                "<br><b>02:30pm</b> &nbsp&nbsp Camp Finale" +
                "<br><b>06:00pm</b> &nbsp&nbsp Dinner" +
                "<br><b>07:30pm</b> &nbsp&nbsp End of Red Camp");
        d2.setBackgroundColor(getResources().getColor(R.color.grey));
        d1.setBackgroundColor(getResources().getColor(R.color.grey));
        d3.setBackgroundColor(getResources().getColor(android.R.color.white));
        content.setText(Html.fromHtml(contents.get(2)));
    }
}
