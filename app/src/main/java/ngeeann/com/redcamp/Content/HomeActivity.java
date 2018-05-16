package ngeeann.com.redcamp.Content;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import ngeeann.com.redcamp.Links;
import ngeeann.com.redcamp.R;
public class HomeActivity extends AppCompatActivity {

    RelativeLayout Btn1,Btn2,Btn3,Btn4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Links links = new Links();

        Btn1 = findViewById(R.id.button1);
        Btn2 = findViewById(R.id.button2);
        Btn3 = findViewById(R.id.button3);
        Btn4 = findViewById(R.id.button4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Btn1.setOnClickListener(v-> startActivity(new Intent(this,WebView.class)
                    .putExtra("links", links.getCamp_programme())
                    .putExtra("name","Camp Programme")));
            Btn3.setOnClickListener(v-> startActivity(new Intent(this,WebView.class)
                    .putExtra("links", links.getCampus_explorer())
                    .putExtra("name","Campus Explorer")));
            Btn2.setOnClickListener(v-> startActivity(new Intent(this,WebView.class)
                    .putExtra("links", links.getCourse_finder())
                    .putExtra("name","Path of Discovery")));
            Btn4.setOnClickListener(v-> startActivity(new Intent(this,WebView.class)
                    .putExtra("links", links.getAsk_red_camp())
                    .putExtra("name","Ask Red Camp")));
        }


    }

}
