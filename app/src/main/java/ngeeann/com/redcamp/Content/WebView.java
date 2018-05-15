package ngeeann.com.redcamp.Content;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ngeeann.com.redcamp.R;

public class WebView extends AppCompatActivity {
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        Intent intent = getIntent();
        String link = intent.getStringExtra("links");
        String getTitle = intent.getStringExtra("name");
        title= findViewById(R.id.title);
        title.setText(getTitle);

        Log.i("WebView Link", link);




    }
}
