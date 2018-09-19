package ngeeann.com.redcamp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ngeeann.com.redcamp.Content.CampProgramme;
import ngeeann.com.redcamp.Content.WebView;
import ngeeann.com.redcamp.SQLiteQuestions.DatabaseHelper;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {
    RelativeLayout Btn1,Btn2,Btn3,Btn4;
    TextView welcomeName;
    public static final String SESSION = "login_status";
    SharedPreferences sessionManager;

    CarouselView carouselView;

    List<Integer> allCarousels  =  new ArrayList(Arrays.asList(R.drawable.carousel1, R.drawable.carousel2, R.drawable.carousel3, R.drawable.carousel4));

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        Btn1 = view.findViewById(R.id.button1);
        Btn2 = view.findViewById(R.id.button2);
        Btn3 = view.findViewById(R.id.button3);
        Btn4 = view.findViewById(R.id.button4);

//        vpCarousell = view.findViewById(R.id.ivCarousell);
//
//        ImageAdapter adapter = new ImageAdapter(this); //Here we are defining the Imageadapter object
//
//        viewPager.setAdapter(adapter); // Here we are passing and setting the adapter for the images

        welcomeName = view.findViewById(R.id.welcomeName);

        carouselView = view.findViewById(R.id.carouselView);




        sessionManager = getContext().getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        Intent getintent = getActivity().getIntent();
        String fullname = getintent.getStringExtra("name");
        if(fullname!=null){
            String[] name = fullname.split(" ");            String firstName = name[0];
            welcomeName.setText(String.format("Hi %s!",firstName));
        }else{
            String[] name =  sessionManager.getString("name", null).split(" ");
            String firstName = name[0];
            welcomeName.setText(String.format("Hi %s!",firstName));
        }


        if(!sessionManager.getString("hasSignedConsent","").isEmpty() && sessionManager.getString("consentRequired","").isEmpty()){
            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(allCarousels.size());
        } else {
            allCarousels.remove(Integer.valueOf(R.drawable.carousel2));
            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(allCarousels.size());
        }






        Links links = new Links();

        Btn1.setOnClickListener((View v)->{
            Intent intent = new Intent(getContext(), CampProgramme.class);
            startActivity(intent);
        });
        Btn2.setOnClickListener((View v)->{
            Intent intent = new Intent(getContext(), WebView.class);
            intent.putExtra("Links", links.getCourse_finder());
            intent.putExtra("name","COURSE FINDER");
            startActivity(intent);
        });
        Btn3.setOnClickListener((View v)->{
            Intent intent = new Intent(getContext(), WebView.class);
            intent.putExtra("Links", links.getCampus_explorer());
            intent.putExtra("name","CAMPUS EXPLORER");
            startActivity(intent);
        });
        Btn4.setOnClickListener((View v)->{
            Intent intent = new Intent(getContext(), WebView.class);
            intent.putExtra("Links", links.getAsk_red_camp());
            intent.putExtra("name","ASK RED CAMP");
            startActivity(intent);
        });
        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(allCarousels.get(position));
        }
    };

}
