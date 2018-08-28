package ngeeann.com.redcamp.SQLiteQuestions;

import java.util.ArrayList;

public class Question {
    private String questionID;
    private String tribe;
    private String question;
    private String[] option = new String[5];
    //private ArrayList<String> option;
    private String userAnswer;

    public Question(){

    }

    public Question(String questionID , String tribe, String question, String[] option, String userAnswer ){
        this.questionID = questionID;
        this.tribe = tribe;
        this.question = question;
        for(int i = 0; i < option.length; i++){
            this.option[i] = option[i] ;
        }
        this.userAnswer = userAnswer;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptionList() {
        return option;
    }

    public void setOptionList(String[] option) {
        this.option = option;
    }

    public String getOption( int i ) {
        return option[i];
    }

    public void setOption(int i, String choice) {
        this.option[i] = choice;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }


}
