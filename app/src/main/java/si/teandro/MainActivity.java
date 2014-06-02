package si.teandro;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.etalio.android.AuthenticationCallback;
import com.etalio.android.EtalioAPI;
import com.etalio.android.client.exception.EtalioAuthorizationCodeException;
import com.etalio.android.client.models.EtalioToken;

public class MainActivity extends ActionBarActivity implements AuthenticationCallback {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String CLIENT_ID = "db14cb16b720ec9b3aab785b1c2b7a78";
    private static final String CLIENT_SECRET = "844351660e3f0aaf1a78c3d55bf8d869";

    private static boolean NOT_SIGN_IN = true;

    private EtalioAPI mEtalio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEtalio = new EtalioAPI(this, CLIENT_ID, CLIENT_SECRET);

        if(NOT_SIGN_IN) {
            mEtalio.initiateEtalioSignIn(this);
            NOT_SIGN_IN = false;
        }

        try {
            boolean isCallback = mEtalio.handleSignInCallback(getIntent(), this);
            Log.d(TAG, "Intent is " + (isCallback ? "" : "not") + " a Etalio authentication callback");
        } catch (EtalioAuthorizationCodeException e) {
            Log.e(TAG, "EtalioAuthorizationCodeException : " + e.getMessage());
        }

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

        EtalioToken token = mEtalio.getEtalioToken();

        if(token != null) {

            RequestTask request = new RequestTask();
            request.execute("http://otro.me/devtest/user", token.getAccessToken());

        }
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
