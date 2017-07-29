package yonifre.com.crimeloggerandroid.communication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ja43705 on 29/07/2017.
 */

public class GetCrimesCommunicator extends AsyncTask<String, String, String> {

    private HttpURLConnection urlConnection;

    @Override
    protected String doInBackground(String... strings) {
        Log.d("CRIMES COMMUNICATOR", "DOING REST CALL");

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("https://agile-mountain-58417.herokuapp.com/crimes");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Authorization-Firebase",strings[0]);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            urlConnection.connect();
            Log.d("CRIMES COMMUNICATOR", "READING");
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.d("CRIMES COMMUNICATOR", result.toString());

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();
    }


    @Override
    protected void onPostExecute(String result) {

        Log.d("CRIMECOMMUNICATOR", result);
        //Do something with the JSON string

    }
}
