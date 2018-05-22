package ngeeann.com.redcamp.Content;

import android.graphics.Color;
import android.os.Build;
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
    TextView content, date;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_programme);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        d1 = findViewById(R.id.day1);
        d2 = findViewById(R.id.day2);
        d3 = findViewById(R.id.day3);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);

        //init
        setd1();
        d1.setOnClickListener(v->setd1());
        d2.setOnClickListener(v->setd2());
        d3.setOnClickListener(v->setd3());

    }
    public void setd1(){
        ArrayList<String> contents = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        dates.add("20 November, Tuesday");
        dates.add("21 November, Wednesday");
        dates.add("22 November, Thursday");


        contents.add("08:30am:     Registration \n" +
                "09:00am:     Event Opening \n" +
                "10:00am:     Path of Discovery \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Path of Discovery 2 \n" +
                "05:30pm:     Course & Admissions Talk \n" +
                "06:00pm:     Mass Dance \n" +
                "06:30pm:     End of Day 1 \n");

        contents.add("08:30am:     Registration \n" +
                "09:00am:     Skit Performance \n" +
                "10:00am:     Path of Discovery 3 \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Path of Discovery 4 \n" +
                "05:30pm:     CCA Fiesta \n" +
                "06:00pm:     Mass Dance \n" +
                "06:30pm:     End of Day 2 \n");

        contents.add( "08:30am:     Registration \n" +
                "09:00am:     Event Opening \n" +
                "10:00am:     Path of Discovery 5 \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Camp Finale \n" +
                "06:00pm:     Dinner \n" +
                "07:30pm:     End of Red Camp \n\n");


        date.setText(dates.get(0));
        content.setText(contents.get(0));
        d2.setTextColor(Color.parseColor("#ffffff"));
        d3.setTextColor(Color.parseColor("#ffffff"));
        d1.setTextColor(getResources().getColor(R.color.red));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            d1.setBackground(getResources().getDrawable(R.drawable.day1));
            d2.setBackground(getResources().getDrawable(R.drawable.day2));
            d3.setBackground(getResources().getDrawable(R.drawable.day3));
        }
        content.setBackgroundColor(Color.parseColor("#ffffff"));
        content.setTextColor(Color.parseColor("#5c5c5c"));

    }
    public void setd2(){
        ArrayList<String> contents = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        dates.add("20 November, Tuesday");
        dates.add("21 November, Wednesday");
        dates.add("22 November, Thursday");


        contents.add("08:30am:     Registration \n" +
                "09:00am:     Event Opening \n" +
                "10:00am:     Path of Discovery \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Path of Discovery 2 \n" +
                "05:30pm:     Course & Admissions Talk \n" +
                "06:00pm:     Mass Dance \n" +
                "06:30pm:     End of Day 1 \n");

        contents.add("08:30am:     Registration \n" +
                "09:00am:     Skit Performance \n" +
                "10:00am:     Path of Discovery 3 \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Path of Discovery 4 \n" +
                "05:30pm:     CCA Fiesta \n" +
                "06:00pm:     Mass Dance \n" +
                "06:30pm:     End of Day 2 \n");

        contents.add( "08:30am:     Registration \n" +
                "09:00am:     Event Opening \n" +
                "10:00am:     Path of Discovery 5 \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Camp Finale \n" +
                "06:00pm:     Dinner \n" +
                "07:30pm:     End of Red Camp \n\n");


        date.setText(dates.get(1));
        content.setText(contents.get(1));
        d1.setTextColor(Color.parseColor("#ffffff"));
        d3.setTextColor(Color.parseColor("#ffffff"));
        d2.setTextColor(getResources().getColor(R.color.red));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            d1.setBackground(getResources().getDrawable(R.drawable.day2_selected_1));
            d2.setBackground(getResources().getDrawable(R.drawable.day2_selected_2));
            d3.setBackground(getResources().getDrawable(R.drawable.day3));
        }
        content.setBackgroundColor(Color.parseColor("#ffffff"));
        content.setTextColor(Color.parseColor("#5c5c5c"));
    }
    public void setd3(){
        ArrayList<String> contents = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        dates.add("20 November, Tuesday");
        dates.add("21 November, Wednesday");
        dates.add("22 November, Thursday");

        contents.add("08:30am:     Registration \n" +
                "09:00am:     Event Opening \n" +
                "10:00am:     Path of Discovery \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Path of Discovery 2 \n" +
                "05:30pm:     Course & Admissions Talk \n" +
                "06:00pm:     Mass Dance \n" +
                "06:30pm:     End of Day 1 \n");

        contents.add("08:30am:     Registration \n" +
                "09:00am:     Skit Performance \n" +
                "10:00am:     Path of Discovery 3 \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Path of Discovery 4 \n" +
                "05:30pm:     CCA Fiesta \n" +
                "06:00pm:     Mass Dance \n" +
                "06:30pm:     End of Day 2 \n");

        contents.add( "08:30am:     Registration \n" +
                "09:00am:     Event Opening \n" +
                "10:00am:     Path of Discovery 5 \n" +
                "01:00pm:     Lunch \n" +
                "02:30pm:     Camp Finale \n" +
                "06:00pm:     Dinner \n" +
                "07:30pm:     End of Red Camp \n\n");

        date.setText(dates.get(2));
        content.setText(contents.get(2));
        d2.setTextColor(Color.parseColor("#ffffff"));
        d1.setTextColor(Color.parseColor("#ffffff"));
        d3.setTextColor(getResources().getColor(R.color.red));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            d1.setBackground(getResources().getDrawable(R.drawable.day3_selected_1));
            d2.setBackground(getResources().getDrawable(R.drawable.day3_selected_2));
            d3.setBackground(getResources().getDrawable(R.drawable.day3_selected_3));
        }
        content.setBackgroundColor(Color.parseColor("#ffffff"));
        content.setTextColor(Color.parseColor("#5c5c5c"));
    }
}
