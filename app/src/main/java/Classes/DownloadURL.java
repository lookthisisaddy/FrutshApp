package Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadURL {

    public Bitmap readUrl(String string) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(string);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bitmap;
    }
}
