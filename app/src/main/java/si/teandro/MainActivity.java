package si.teandro;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.etalio.android.AuthenticationCallback;
import com.etalio.android.EtalioAPI;
import com.etalio.android.client.exception.EtalioAuthorizationCodeException;
import com.etalio.android.client.models.EtalioToken;
import com.etalio.android.client.models.User;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import si.teandro.model.EtalioUser;

public class MainActivity extends ActionBarActivity implements AuthenticationCallback {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String CLIENT_ID = "db14cb16b720ec9b3aab785b1c2b7a78";
    private static final String CLIENT_SECRET = "844351660e3f0aaf1a78c3d55bf8d869";

    private static boolean NOT_SIGN_IN = true;

    Handler handler = new Handler();

    private EtalioAPI mEtalio;
    private static EtalioUser profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mEtalio = new EtalioAPI(this, CLIENT_ID, CLIENT_SECRET);

        if (NOT_SIGN_IN) {
            mEtalio.initiateEtalioSignIn(this);
            NOT_SIGN_IN = false;
        }

        try {
            boolean isCallback = mEtalio.handleSignInCallback(getIntent(), this);
            Log.d(TAG, "Intent is " + (isCallback ? "" : "not") + " a Etalio authentication callback");

            if(isCallback) {
                EtalioToken token = mEtalio.getEtalioToken();

                if (token != null) {

                    RequestTask request = new RequestTask();
                    request.execute("http://otro.me/devtest/user", token.getAccessToken()).get();
                    //profile = request.getProfile();

                    if (profile == null)
                        Log.i(TAG, "Profil se ni napolnil !");

                }
            }
        } catch (EtalioAuthorizationCodeException e) {
            Log.e(TAG, "EtalioAuthorizationCodeException : " + e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException : " + e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException : " + e.getMessage());
        }


            setContentView(R.layout.activity_main);






        // setting values

        if(profile == null)
            Log.i(TAG, "Profil je prazen !");
        if(profile != null) {
            Log.i(TAG, "Profil je POLN !");
            ((TextView) findViewById(R.id.profile_name)).setText(profile.getName());
            ((TextView) findViewById(R.id.profile_age)).setText("Age : " + profile.getAge());
            ((TextView) findViewById(R.id.profile_location)).setText(profile.getLocation());
            ((TextView) findViewById(R.id.profile_wisdom)).setText(profile.getBio());
        }

        View currentView = (View) findViewById(R.id.activity_home_screen);
        changeBackgroundColor(currentView);
        inflateImage();




        //currentView.startAnimation(AnimationUtils.);

        //R.string.profile_age



    }

    private void inflateImage() {

        final ImageView image = (ImageView) findViewById(R.id.test_image);
        final Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.profil);

        (new Thread(){
            @Override
            public void run(){
                for(int i=10; i<100; i = i+10){
                    final int f = i;
                    handler.post(new Runnable(){
                        public void run(){
                            Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, f, f, true);
                            image.setImageBitmap(bMapScaled);
                        }
                    });
                    // next will pause the thread for some time
                    try{ sleep(100); }
                    catch(InterruptedException e) { break; }
                }
            }
        }).start();

    }


    private void changeBackgroundColor(final View currentView) {

        (new Thread(){
            @Override
            public void run(){
                for(int i=0; i<255; i++){
                    final int f = i;
                    handler.post(new Runnable(){
                        public void run(){
                            currentView.setBackgroundColor(Color.argb(255, 255-f, 255-f, 255-f));
                        }
                    });
                    // next will pause the thread for some time
                    try{ sleep(10); }
                    catch(InterruptedException e) { break; }
                }
            }
        }).start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Handle result from native SSO which occurs if Etalio app is installed.
        mEtalio.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onAuthenticationSuccess() {
        Log.i(TAG, "onAuthenticationSuccess : Authentication succeeded");

        /*
        EtalioToken token = mEtalio.getEtalioToken();

        if(token != null) {

            RequestTask request = new RequestTask();
            request.execute("http://otro.me/devtest/user", token.getAccessToken());
            //profile = request.getProfile();

            if(profile == null)
                Log.i(TAG, "Profil se ni napolnil !");

        }
        */
    }

    @Override
    public void onAuthenticationFailure(Exception exception) {
        Log.i(TAG,"onAuthenticationFailure : "+exception.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class RequestTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;

            HttpPost post = new HttpPost(uri[0]);
            post.addHeader("token",uri[1]);

            try {
                response = httpclient.execute(post);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }


        Log.i(TAG, responseString);

        Gson gson = new Gson();
        profile = gson.fromJson(responseString, EtalioUser.class);


            //Log.i(TAG,"Name : "+profile.getName());

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            /*
            Log.i(TAG, result);

            Gson gson = new Gson();
            profile = gson.fromJson(result, EtalioUser.class);
            Log.i(TAG, profile.toString());
            */
        }

    }
}
