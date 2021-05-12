package Classes;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap[]>{

    private static final String TAG = "ImageDownloader";

    @Override
    protected Bitmap[] doInBackground(String... urls) {
        Bitmap[] bitmaps = new Bitmap[urls.length];
        for (int i = 0; i < urls.length; i++){
            try {
                URL url = new URL(urls[i]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                bitmaps[i] = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmaps;
    }

}
