package ngeeann.com.redcamp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Content.MainActivity;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.SQLiteQuestions.DatabaseHelper;
import ngeeann.com.redcamp.SQLiteQuestions.Notification;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            addNotification(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String messageBody) {
        Log.d(TAG, "Message sendNotification: " + messageBody);
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "NPRC_CH_ID")
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("REDCAMP")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("NPRC_CH_ID",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void addNotification(String messageString){
//        SharedPreferences sessionManager = getSharedPreferences("login_status", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sessionManager.edit();
//        if(sessionManager.contains("notifications")){
//            try{
//                JSONArray notificationsArr = new JSONArray(sessionManager.getString("notifications",""));
//                notificationsArr.put(message);
//
//                editor.putString("notifications",notificationsArr.toString());
//                editor.putBoolean("hasNewNotifications",true);
//                editor.commit();
//                Log.i("allnotifications",notificationsArr.toString());
//            } catch (JSONException e){
//                e.printStackTrace();
//            }
//        } else {
//            JSONArray notificationsArr = new JSONArray();
//            notificationsArr.put(message);
//            editor.putString("notifications",notificationsArr.toString());
//            editor.putBoolean("hasNewNotifications",true);
//            editor.commit();
//            Log.i("allnotifications",notificationsArr.toString());
//        }

        Log.d(TAG, "Message addNotification: " + messageString);
        DatabaseHelper myDatabaseHelper = new DatabaseHelper(this);
        try{

            Notification n = new Notification();

            JSONObject nObj = new JSONObject(messageString);
            Log.i("message received",messageString);
            String type = nObj.getString("type");
            String isRead = nObj.getString("isRead");
            String title = nObj.getString("title");
            String message = nObj.getString("message");
            String dateReceived = nObj.getString("dateReceived");

            n.setType(type);
            n.setIsRead(isRead);
            n.setTitle(title);
            n.setMessage(message);
            n.setDatetimeReceived(dateReceived);
            myDatabaseHelper.addNotification(n);

        } catch (JSONException e){
            e.printStackTrace();
        }

    }
}
