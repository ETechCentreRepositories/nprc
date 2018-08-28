package ngeeann.com.redcamp.SQLiteQuestions;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionBank {

    List<Question> list = new ArrayList<>();
    DatabaseHelper myDatabaseHelper;

    public int getLength(){ return list.size(); }

    public String getQuestion(int a){ return list.get(a).getQuestion(); }

    public String getChoice(int index, int num){ return list.get(index).getOption(num-1); }

//    public void updateUserAnswer(String tribe, String userAnswer, Context context){
//        myDatabaseHelper = new DatabaseHelper(context);
//        myDatabaseHelper.updateUserAnswer(tribe, userAnswer);
//
//
//    }

    public void initQuestions(Context context){
        myDatabaseHelper = new DatabaseHelper(context);
        list = myDatabaseHelper.getAllQuestionsList();

        if(list.isEmpty()){
            //myDatabaseHelper.addInitialQuestion(new Question("0","1","quiz","Who is the Principal of NP?",new String[]{"Halimah Yacob","Clarence Ti Boon Wee","Soh Wai Wah","Jeanne Liew","Tony Tan"},"Clarence Ti Boon Wee",null,"false"));
            myDatabaseHelper.addInitialQuestion(new Question("1" ,"1a","Laser show was …", new String[]{"LIT!","So-So","Meh","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("2" ,"1b","Was the Admissions Talk helpful?", new String[]{"Yuppers!","It’s alright. ","I fell asleep…","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("3" ,"2a","Were the CCA performances dope?", new String[]{"They were slaying it!","So-So","Nah…","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("4" ,"2b","I’m excited for Camp Finale tomorrow!", new String[]{"Totes! ","Quite","Meh","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("5" ,"3a","Thoughts on the RED Camp 15 skit?", new String[]{"Amazeballs!","Quite standard","Yawns…","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("6" ,"3b","Mass Dance - YAY or NAY?", new String[]{"Yay!","So-So","Nay!","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("7" ,"3c","Oh My! Camp Finale was...", new String[]{"SO LIT!! I LOVE IT! ","Ok only leh","Meh","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("8" ,"3d","Pick one that describes your SLs!", new String[]{"G.O.A.T! I love them!","Not quite there yet","CMI","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("9" ,"3e","I came for RED Camp because...", new String[]{"I LOVE NP. And, RED Camp is lit!","I wanna learn more about NP courses","Dunno if Poly or JC is more suitable for me so I’m checking it out","My squad jio me so I just came along",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("10" ,"3f","I feel part of NP Fam already! ", new String[]{"Totes!","Gettin' there...","Not really","",""},null));

            myDatabaseHelper.addInitialQuestion(new Question("11" ,"ba1","What do you like most about our School Of Business & Accountancy (BA)?", new String[]{"BA is the most established business school amongst the polys!","Students sharing their experiences","The Advertising Challenge - learning about marketing strategies (and the skit activity!)","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("12" ,"ba2","How was your experience at BA today?", new String[]{"Good!","It was alright","Bad","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("13" ,"de1","Was it fun building your own lamp at School of Design & Environment (DE)?", new String[]{"My lamp was LIT!","So-So","No feels for me","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("14" ,"de2","First impression of the DE fam?", new String[]{"Very rah rah!","Quite friendly","Cold","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("15" ,"fms1","____________ at School of Film & Media Studies was the most eye-opening tour for me!", new String[]{"Radio Heatwave & M:idea Office","Green Screen Studio","TV Studio","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("16" ,"fms2","Would you rather work behind the scene as a “Clark Kent” type super reporter, or behind the camera as a “Flash” camera operator?  ", new String[]{"Clark Kent","Flash","","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("17" ,"soe1","I had a great time at School of Engineering (SoE)!", new String[]{"Totally fun!","Not too bad","Boring...too basic","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("18" ,"soe2","My favourite activity at SoE was...", new String[]{"Programming the lights and generating the electrical power for the car!","Building the tower and the motor that moves the car along!","","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("19" ,"ict1","Which would you choose to do? Develop a world famous game or protect the country from cyber-attacks?", new String[]{"World famous game – I can bring fun to many people through my creativity!","Cybersecurity expert – Though I may go unrecognized, I could prevent devastation!","","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("20" ,"ict2","If you could pick one talent, what would it be?", new String[]{"Insane drawing skills","Immensely creative flair","Strong analytical mind","",""},null));

            myDatabaseHelper.addInitialQuestion(new Question("21" ,"hms1","First impression of School of Humanities & Social Sciences (HMS) peeps?", new String[]{"Awesome!","Full of goodness in their hearts","Talented!","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("22" ,"hms2","I like this activity at HMS the most!", new String[]{"Moppets! The puppets are cute!","HMS Go!” Game - I got to know more about the courses offered!","Who is this?” Game - I got to know about possible career pathways!","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("23" ,"lsct1","School of Life Sciences & Chemical Technology (LSCT) can equip me with the skills and knowledge to make a difference in the world.", new String[]{"Yes, I want to be an LSCT graduate!","Maybe, I’m not sure now","Mmm...not for me","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("24" ,"lsct2","I had a great time at LSCT!", new String[]{"Absolutely!","I needed more time! I didn’t manage to try out all the fun activities.","It was a tad too draggy...","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("25" ,"hs1","I now know what basic healthcare knowledge and skills are all about! ", new String[]{"Absolutely! School of Health Sciences (HS) showed me loads!","Yup, clearer now!","Actually, I'm still not so sure","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("26" ,"hs2","How do you feel about the activities at the School of Health Sciences (HS)? ", new String[]{"So interesting! Awesome :)","Good!","I almost zzzzzzzz","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("27" ,"ccas1","One word to describe the CCA Showcase & Tryouts?", new String[]{"SUPERB","Typical","Boring","",""},null));
            myDatabaseHelper.addInitialQuestion(new Question("28" ,"ccas2","Say you’re a first-year student at NP - What CCAs are you most likely to join?", new String[]{"Dance - I want to pick up some cool moves!","Martial Arts – I want to learn some awesome kicks!","Community Service – I want to give back to society!","",""},null));

            list = myDatabaseHelper.getAllQuestionsList();
        }
    }
}
