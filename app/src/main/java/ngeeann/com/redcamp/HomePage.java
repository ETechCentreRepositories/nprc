package ngeeann.com.redcamp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ngeeann.com.redcamp.Content.CampProgramme;
import ngeeann.com.redcamp.Content.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {
    RelativeLayout Btn1,Btn2,Btn3,Btn4;
    TextView welcomeName;
    public static final String SESSION = "login_status";
    SharedPreferences sessionManager;


    public HomePage() {
        // Required empty public constructor
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

        welcomeName = view.findViewById(R.id.welcomeName);
        sessionManager = getContext().getSharedPreferences(SESSION, Context.MODE_PRIVATE);

        Intent getintent = getActivity().getIntent();
        String fullname = getintent.getStringExtra("name");
        if(fullname!=null){
            String[] name = fullname.split(" ");
            String firstName = name[0];
            welcomeName.setText(String.format("Hi %s!",firstName));
        }else{
            String[] name =  sessionManager.getString("name", null).split(" ");
            String firstName = name[0];
            welcomeName.setText(String.format("Hi %s!",firstName));
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

}
