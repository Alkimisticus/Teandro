package si.teandro;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.etalio.android.AuthenticationCallback;
import com.etalio.android.EtalioAPI;
import com.etalio.android.EtalioPersistentStore;
import com.etalio.android.client.exception.EtalioAuthorizationCodeException;
import com.etalio.android.client.exception.EtalioHttpException;
import com.etalio.android.client.exception.EtalioTokenException;
import com.etalio.android.client.models.EtalioToken;
import com.etalio.android.client.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends ActionBarActivity implements AuthenticationCallback {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String CLIENT_ID = "26d33e7028e6a2b61b2c811d1324bf88";
    private static final String CLIENT_SECRET = "71bc61901f46445fdb3db291c99f4ce0";

    private static boolean niprijavlen = true;

    private EtalioAPI mEtalio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEtalio = new EtalioAPI(this, "db14cb16b720ec9b3aab785b1c2b7a78", "844351660e3f0aaf1a78c3d55bf8d869");

        if(niprijavlen) {
            mEtalio.initiateEtalioSignIn(this);
            niprijavlen = false;
        }


        Log.i("MainActivity","initiateEtalioSignIn");

        /*
        String state = mEtalio.getPersistentData(EtalioPersistentStore.SupportedKey.STATE);
        Log.i(TAG,"STATE :"+state);
        */

        // Or use your scopes
        // mEtalio.initiateEtalioLogin(this, "profile.basic.r profile.email.r");

        try {
            boolean isCallback = mEtalio.handleSignInCallback(getIntent(), this);
            Log.d(TAG, "Intent is " + (isCallback ? "" : "not") + " a Etalio authentication callback");
        } catch (EtalioAuthorizationCodeException e) {
            Log.e("MainActivity", "EtalioAuthorizationCodeException : " + e.getMessage());
        }

        /*
        Log.i(TAG, "IsAutheticated :" + mEtalio.isAuthenticated());

        EtalioToken token = mEtalio.getEtalioToken();
        if(token != null)
            Log.i(TAG,token.getAccessToken());
        */

        /*
        RequestTask request = new RequestTask();
        request.execute("http://otro.me/devtest/user");
        */

        /*
        try {

        User user = mEtalio.getCurrentProfile();
        } catch (IOException e) {
            Log.e(TAG, "IOException : " + e.getMessage());
        } catch (EtalioHttpException e) {
            Log.e(TAG, "EtalioHttpException : " + e.getMessage());
        } catch (EtalioTokenException e) {
            Log.e(TAG, "EtalioTokenException : " + e.getMessage());
        }
        */

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Handle result from native SSO which occurs if Etalio app is installed.
        mEtalio.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onAuthenticationSuccess() {
        Log.i(TAG, "onAuthenticationSuccess : Authentication succeeded");

        //Log.i(TAG, "IsAutheticated :" + mEtalio.isAuthenticated());

        EtalioToken token = mEtalio.getEtalioToken();
        if(token != null)
            Log.i(TAG,"TOKEN koncno : "+token.getAccessToken());

        RequestTask request = new RequestTask();
        request.execute("http://otro.me/devtest/user",token.getAccessToken());

        /* network exception
        try {

            User user = mEtalio.getCurrentProfile();
            Log.i(TAG,user.getName());
        } catch (IOException e) {
            Log.e(TAG, "IOException : " + e.getMessage());
        } catch (EtalioHttpException e) {
            Log.e(TAG, "EtalioHttpException : " + e.getMessage());
        } catch (EtalioTokenException e) {
            Log.e(TAG, "EtalioTokenException : " + e.getMessage());
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

}
