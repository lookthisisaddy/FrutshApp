package Classes;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWebContent extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {

        String result = "";

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();
            while (data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
