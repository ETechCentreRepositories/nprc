package ngeeann.com.redcamp.connection;

import android.os.AsyncTask;

/**
 * Created by user on 22/3/2018.
 */

public class getAsyncRequest extends AsyncTask<String, Void , String>{
    OnAsyncResult onAsyncResult;

    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpRequest req = new HttpRequest();
        String result = req.GetRequest(strings[0]);
        if(!result.isEmpty()){
            return result;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        onAsyncResult.result(0, result);
    }

    public interface OnAsyncResult {
        void result(int resultCode, String message);
    }
}