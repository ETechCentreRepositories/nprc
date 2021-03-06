package ngeeann.com.redcamp.NavigationItems;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import ngeeann.com.redcamp.Content.Home;
import ngeeann.com.redcamp.Login.LoginLauncher;
import ngeeann.com.redcamp.R;
import ngeeann.com.redcamp.connection.HttpRequest;

public class signature_activity extends AppCompatActivity {

    Button mGetSign;
    ImageView mClear;
    File file;
    LinearLayout mContent;
    View view;
    signature mSignature;
    Bitmap bitmap;
    public static final String SESSION = "login_status";
    public static final String SESSION_ID = "session";
    SharedPreferences sessionManager;
    Toolbar toolbar;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_activity);

        mContent = (LinearLayout) findViewById(R.id.canvasLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);

        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = findViewById(R.id.ivClear);
        mGetSign = findViewById(R.id.btnSubmitSignature);
        mGetSign.setEnabled(false);
        view = mContent;
        mGetSign.setOnClickListener(onButtonClick);
        mClear.setOnClickListener(onButtonClick);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == mClear) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            } else if (v == mGetSign) {
                Log.v("log_tag", "Panel Saved");
                if (Build.VERSION.SDK_INT >= 23) {
                    if(isStoragePermissionGranted()){
                        String signature = mSignature.save(view, StoredPath);
                        if (!signature.equals("")){
                            Log.e("image encoded", signature);
                            SubmitConsent submitConsent = new SubmitConsent();
                            try {
                                String update = submitConsent.execute(signature).get();

                                // encrypt as QR

                                AlertDialog.Builder dialogQR = new AlertDialog.Builder(signature_activity.this);
                                LayoutInflater li = LayoutInflater.from(signature_activity.this);
                                final View gtnc = li.inflate(R.layout.dialog_qr_code_aftersign ,null);
                                ImageView qrcode = gtnc.findViewById(R.id.ivDialogQRCode);
                                Button btnQRHome = gtnc.findViewById(R.id.btnQRHome);

                                btnQRHome.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(signature_activity.this,Home.class));
                                        finish();
                                    }
                                });

                                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);

                                JSONObject jsonProfile = new JSONObject();
                                try{
                                    jsonProfile.put("id",sessionManager.getString("id","no id"));
                                    jsonProfile.put("name",sessionManager.getString("name","Not added"));
                                    jsonProfile.put("email",sessionManager.getString("email","Not added"));
                                    jsonProfile.put("mobile",sessionManager.getString("contact","Not added"));
                                    jsonProfile.put("tribe",sessionManager.getString("tribe","Not added"));
                                    jsonProfile.put("nric",sessionManager.getString("nric","Not added"));
                                    jsonProfile.put("dob",sessionManager.getString("dob","Not assigned yet"));
                                    jsonProfile.put("parentConsent",sessionManager.getString("hasSignedConsent","").isEmpty());
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                String text = jsonProfile.toString();
                                //String text="Damian lee"; // qr code to be in json string
                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                try {
                                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                    qrcode.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }

                                dialogQR.setCancelable(false);
                                dialogQR.setView(gtnc);
                                AlertDialog dialogueQR = dialogQR.create();
                                dialogueQR.show();


                                Log.e("UPDATING CONSENT",update);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                } else {
                    view.setDrawingCacheEnabled(true);
                    mSignature.save(view, StoredPath);
                    Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                    // Calling the same class
                    //recreate();
                }

            }
        }
    };


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            view.setDrawingCacheEnabled(true);
            mSignature.save(view, StoredPath);
            Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
            // Calling the same class
            recreate();
        }
        else
        {
            Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
        }
    }

    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public String save(View v, String StoredPath) {
            String encodedImage = "";
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                Log.v("log_tag","try upload img");
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
//                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
//                Intent intent = new Intent(signature_activity.this, parent_consent_form.class);
//                intent.putExtra("imagePath", StoredPath);
//                startActivity(intent);
//                finish();
//                Toast.makeText(getApplicationContext(),StoredPath,Toast.LENGTH_SHORT);
//                mFileOutStream.flush();
//                mFileOutStream.close();

                File f = null;
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File file = new File(Environment.getExternalStorageDirectory(),"RCImages_cache");
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    f = new File(file.getAbsolutePath()+file.separator+ "redcampsign"+".png");
                }
                FileOutputStream ostream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, ostream);
                ostream.close();

                encodedImage = bitmapToBase64(bitmap);
                Log.e("BITMAP: ", encodedImage);


                MediaScannerConnection.scanFile(getApplicationContext(), new String[] { f.toString() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.v("log_tag", "image is saved in gallery and gallery is refreshed.");
                            }
                        }
                );

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
            return encodedImage;
        }

        public void clear() {
            path.reset();
            invalidate();
            mGetSign.setEnabled(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

        //convert bitmap to string to be stored in database
        private String bitmapToBase64(Bitmap bitmap) {
            String encodedImage = "";
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                encodedImage += URLEncoder.encode(Base64.encodeToString(byteArray, Base64.DEFAULT), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return encodedImage;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkTimeOut();
    }

    private void checkTimeOut(){
        sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        if(sessionManager.contains("timedOut")){
            if(sessionManager.getBoolean("timedOut",false)){
                sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessionManager.edit();
                editor.putString(SESSION_ID, "400");
                editor.apply();
                startActivity(new Intent(signature_activity.this, LoginLauncher.class));
                finish();
            }
        }
    }

    public class SubmitConsent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpRequest request= new HttpRequest();
            sessionManager = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
            String userid = sessionManager.getString("id","");
            Log.e("USER ID", userid);
            Intent i= getIntent();
            String name = i.getStringExtra("name");
            String number = i.getStringExtra("mobile");
            String relationship = i.getStringExtra("relationship");
            String signature = strings[0];
            String update = request.PostRequest("https://www1.np.edu.sg/npnet/MobileApi/api/Login/parentConcern/?userid="+userid+"&parentName="+name+"&parentMobile="+number+"parentRelation="+relationship+"&parentSign="+signature,"");
            return update;
        }
    }
}
