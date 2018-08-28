package ngeeann.com.redcamp.SQLiteQuestions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    static final String DB_NAME = "nprc.DB";

    // database version
    static final int DB_VERSION = 1;

    // Table Name
    public static final String TABLE_QUESTION = "table_questions";

    // Table columns
    public static final String ID = "questionID";
    //5 different tribes and "all" for generic questions
    public static final String TRIBE = "tribe";
    public static final String QUESTION = "question";
    public static final String OPTION1 = "option1";
    public static final String OPTION2 = "option2";
    public static final String OPTION3 = "option3";
    public static final String OPTION4 = "option4";
    public static final String OPTION5 = "option5";
    //columns below by default is null
    public static final String USERANSWER = "userAnswer";


    //Notifications table
    public static final String TABLE_NOTIFICATION = "table_notifications";

    public static final String NOTIFICATIONID = "notificationID";
    public static final String TYPE = "type";
    public static final String ISREAD = "isRead";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String DATERECEIVED = "dateReceived";


    // Creating table query
//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
//            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + DESC + " TEXT);";

    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
            + TABLE_QUESTION + "(" + ID
            + " INTEGER PRIMARY KEY, "
            + TRIBE + " TEXT, "
            + QUESTION + " TEXT, "
            + OPTION1 + " TEXT, " + OPTION2 + " TEXT, " + OPTION3 + " TEXT, "
            + OPTION4 + " TEXT, " + OPTION5 + " TEXT, "
            + USERANSWER + " TEXT); ";


    private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE "
            + TABLE_NOTIFICATION + "(" + NOTIFICATIONID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT, "
            + ISREAD + " TEXT, "
            + TITLE + " TEXT, "
            + MESSAGE + " TEXT, "
            + DATERECEIVED + " TEXT); ";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_QUESTION + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_NOTIFICATION + "'");
        onCreate(db);
    }

    public long addInitialQuestion (Question question){
        SQLiteDatabase db = this.getWritableDatabase();
        //Creating content values
        ContentValues values = new ContentValues();
        values.put(ID, question.getQuestionID());
        values.put(TRIBE, question.getTribe());
        values.put(QUESTION, question.getQuestion());
        values.put(OPTION1, question.getOption(0));
        values.put(OPTION2, question.getOption(1));
        values.put(OPTION3, question.getOption(2));
        values.put(OPTION4, question.getOption(3));
        values.put(OPTION5, question.getOption(4));
        values.put(USERANSWER, question.getUserAnswer());

        long insert = db.insert(TABLE_QUESTION, null, values);

        return insert;

    }

    public long addNotification (Notification notification){
        Log.i("DatabaseHelper", " Adding Notification");
        SQLiteDatabase db = this.getWritableDatabase();
        //Creating content values
        ContentValues values = new ContentValues();
        values.put(TYPE, notification.getType());
        values.put(ISREAD, notification.getIsRead());
        values.put(TITLE, notification.getTitle());
        values.put(MESSAGE, notification.getMessage());
        values.put(DATERECEIVED, notification.getDatetimeReceived());

        long insert = db.insert(TABLE_NOTIFICATION, null, values);

        return insert;

    }

    public int getNumberOfNewNotification(){
        String getQuery = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + ISREAD + " = 'false'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(getQuery, null);
        int count = c.getCount();
        c.close();

        return count;
    }

    public List<Notification> getAllNotification(){
        List<Notification> notificationArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATION + " ORDER BY " + NOTIFICATIONID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Log.i("Cursor data: ",DatabaseUtils.dumpCursorToString(c));
        //loop through all and add to list
        if(c.moveToFirst()) {
            do {
                Notification n = new Notification();

                int notificationid = c.getInt(c.getColumnIndex(NOTIFICATIONID));
                n.setNotificationID(notificationid);

                String type = c.getString(c.getColumnIndex(TYPE));
                n.setType(type);

                String isRead = c.getString(c.getColumnIndex(ISREAD));
                n.setIsRead(isRead);

                String title = c.getString(c.getColumnIndex(TITLE));
                n.setTitle(title);

                String message = c.getString(c.getColumnIndex(MESSAGE));
                n.setMessage(message);

                String datereceived = c.getString(c.getColumnIndex(DATERECEIVED));
                n.setDatetimeReceived(datereceived);

                //add to list
                notificationArrayList.add(n);

            } while (c.moveToNext());

        }
        Log.i("DatabaseHelper List: ", notificationArrayList.toString());
        return notificationArrayList;
    }

    public Boolean updateIsRead(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.i("updateIsRead ", id);
        cv.put("isRead", "true");
        int num_of_rows_updated = db.update(TABLE_NOTIFICATION, cv, "notificationID = ?", new String[]{id});

        if(num_of_rows_updated > 0){
            return true;
        } else {
            return false;
        }

    }

    public Boolean updateUserAnswer(String tribe, String userAnswer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("userAnswer",userAnswer);
        int num_of_rows_updated = db.update(TABLE_QUESTION, cv, "tribe = ?", new String[]{tribe});

        if(num_of_rows_updated > 0){
            return true;
        } else {
            return false;
        }

    }

    public List<Question> getAllQuestionsList() {
        List<Question> questionArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //loop through all and add to list
        if(c.moveToFirst()){
            do {
                Question q  = new Question();

                String id = c.getString(c.getColumnIndex(ID));
                q.setQuestionID(id);

                String tribe = c.getString(c.getColumnIndex(TRIBE));
                q.setTribe(tribe);

                String question = c.getString(c.getColumnIndex(QUESTION));
                q.setQuestion(question);

                String option1 = c.getString(c.getColumnIndex(OPTION1));
                q.setOption(0, option1);

                String option2 = c.getString(c.getColumnIndex(OPTION2));
                q.setOption(1, option2);

                String option3 = c.getString(c.getColumnIndex(OPTION3));
                q.setOption(2, option3);

                String option4 = c.getString(c.getColumnIndex(OPTION4));
                q.setOption(3, option4);

                String option5 = c.getString(c.getColumnIndex(OPTION5));
                q.setOption(4, option5);

                String userAnswer = c.getString(c.getColumnIndex(USERANSWER));
                q.setUserAnswer(userAnswer);

                //add to list
                questionArrayList.add(q);

            } while (c.moveToNext());
        }

        return questionArrayList;
    }
}

