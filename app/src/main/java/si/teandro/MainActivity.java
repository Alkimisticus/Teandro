package si.teandro;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TableRow;
import android.widget.TextView;

import com.etalio.android.AuthenticationCallback;
import com.etalio.android.EtalioAPI;
import com.etalio.android.client.exception.EtalioAuthorizationCodeException;
import com.etalio.android.client.models.EtalioToken;
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
            //TODO Potrebno bi bilo narediti preko kakšne interne shrambe, ne kr na inicializacijo razreda
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

                    //TODO Potrebno bi bilo naredi asinhrono
                    request.execute("http://otro.me/devtest/user", token.getAccessToken()).get();

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

        if(profile != null) {

            Typeface font_normal = Typeface.createFromAsset(getAssets(), "NotoSansUI-Regular.ttf");

            TextView profile_name = (TextView) findViewById(R.id.profile_name);
            profile_name.setText(profile.getName());
            profile_name.setTypeface(font_normal,Typeface.BOLD);

            TextView profile_age = (TextView) findViewById(R.id.profile_age);
            //TODO To ni vredu, ker ni lokalizirano
            profile_age.setText("Age " + profile.getAge());
            profile_age.setTypeface(font_normal,Typeface.NORMAL);

            TextView profile_location = (TextView) findViewById(R.id.profile_location);
            profile_location.setText(profile.getLocation());
            profile_location.setTypeface(font_normal,Typeface.NORMAL);

            TextView profile_bio = (TextView) findViewById(R.id.profile_wisdom);
            profile_bio.setText(profile.getBio());
            profile_bio.setTypeface(font_normal, Typeface.NORMAL);

            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.abc_slide_out_bottom);

            TableRow row1 = (TableRow) findViewById(R.id.row1);
            row1.setVisibility(View.INVISIBLE);
            row1.setAnimation(animation);
            TableRow row2 = (TableRow) findViewById(R.id.row2);
            row2.setVisibility(View.INVISIBLE);
            row2.setAnimation(animation);
            TableRow row3 = (TableRow) findViewById(R.id.row3);
            row3.setVisibility(View.INVISIBLE);
            row3.setAnimation(animation);
            TableRow row4 = (TableRow) findViewById(R.id.row4);
            row4.setVisibility(View.INVISIBLE);
            row4.setAnimation(animation);
            TableRow row5 = (TableRow) findViewById(R.id.row5);
            row5.setVisibility(View.INVISIBLE);
            row5.setAnimation(animation);
            TableRow row6 = (TableRow) findViewById(R.id.row6);
            row6.setVisibility(View.INVISIBLE);
            row6.setAnimation(animation);
            TableRow row7 = (TableRow) findViewById(R.id.row7);
            row7.setVisibility(View.INVISIBLE);
            row7.setAnimation(animation);
            TableRow row8 = (TableRow) findViewById(R.id.row8);
            row8.setVisibility(View.INVISIBLE);
            row8.setAnimation(animation);
            TableRow row9 = (TableRow) findViewById(R.id.row9);
            row9.setVisibility(View.INVISIBLE);
            row9.setAnimation(animation);

            View currentView = findViewById(R.id.activity_home_screen);
            changeBackgroundColor(currentView,profile.getCharacter().getBody().getColor());

            inflateImage();

            animation.startNow();

            row1.setVisibility(View.VISIBLE);
            row2.setVisibility(View.VISIBLE);
            row3.setVisibility(View.VISIBLE);
            row4.setVisibility(View.VISIBLE);
            row5.setVisibility(View.VISIBLE);
            row6.setVisibility(View.VISIBLE);
            row7.setVisibility(View.VISIBLE);
            row8.setVisibility(View.VISIBLE);
            row9.setVisibility(View.VISIBLE);

        }

    }

    private void inflateImage() {

        final ImageView image = (ImageView) findViewById(R.id.test_image);
        final Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.profil);

        (new Thread(){
            @Override
            public void run(){
                for(int i=10; i<400; i = i+10){
                    final int f = i;
                    handler.post(new Runnable(){
                        public void run(){
                            Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, f, f, true);
                            image.setImageBitmap(bMapScaled);
                        }
                    });
                    // next will pause the thread for some time
                    try{ sleep(50); }
                    catch(InterruptedException e) { break; }
                }
            }
        }).start();

    }


    private void changeBackgroundColor(final View currentView, String body_color) {


        final int red = Integer.valueOf( body_color.substring( 0, 2 ), 16 );
        final int green = Integer.valueOf( body_color.substring( 2, 4 ), 16 );
        final int blue = Integer.valueOf( body_color.substring( 4, 6 ), 16 );

        (new Thread(){
            @Override
            public void run(){
                for(int i=0; i<255; i++){
                    final int f = i;
                    handler.post(new Runnable(){
                        public void run(){
                            currentView.setBackgroundColor(Color.argb(f, red, green, blue));
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

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
}
