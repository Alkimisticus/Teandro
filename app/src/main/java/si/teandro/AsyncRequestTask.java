package si.teandro;

import android.os.AsyncTask;
import android.util.Log;

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

import si.teandro.model.EtalioUser;

/**
 * Created by jernej on 31.5.14.
 */
class AsyncRequestTask extends AsyncTask<String, String, String> {

    private static final String TAG = AsyncRequestTask.class.getCanonicalName();
    private EtalioUser profile;

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

        /*
        Log.i(TAG, responseString);

        Gson gson = new Gson();
        profile = gson.fromJson(responseString, EtalioUser.class);
        */

        //Log.i(TAG,"Name : "+profile.getName());

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.i(TAG, result);

        Gson gson = new Gson();
        profile = gson.fromJson(result, EtalioUser.class);

    }

    public EtalioUser getProfile() {
        return profile;
    }
}