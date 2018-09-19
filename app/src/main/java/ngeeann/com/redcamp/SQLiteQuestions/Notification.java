package ngeeann.com.redcamp.SQLiteQuestions;

public class Notification {
    private int notificationID;
    private String type;
    private String isRead;
    private String title;
    private String message;
    private String datetimeReceived;
    private int userid;

    public String getData() {
        return data;
    }

    private String data;

    public Notification(){

    }

    public Notification(int id , String message, String title, String data, int userid){
        this.notificationID = id;
        this.message = message;
        this.title = title;
        this.data = data;
        this.userid = userid;

    }

    public Notification(int notificationID, String type, String isRead, String title, String message, String datetimeReceived) {
        this.notificationID = notificationID;
        this.type = type;
        this.isRead = isRead;
        this.title = title;
        this.message = message;
        this.datetimeReceived = datetimeReceived;
    }
    public Notification(int notificationID, String type, String isRead, String title, String message) {
        this.notificationID = notificationID;
        this.type = type;
        this.isRead = isRead;
        this.title = title;
        this.message = message;
        this.datetimeReceived = datetimeReceived;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetimeReceived() {
        return datetimeReceived;
    }

    public void setDatetimeReceived(String datetimeReceived) {
        this.datetimeReceived = datetimeReceived;
    }

}
