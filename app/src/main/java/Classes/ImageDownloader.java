package Classes;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.IOException;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap[]> {

    private static final String TAG = "ImageDownloader";

    @Override
    protected Bitmap[] doInBackground(String... urls) {

        DownloadURL downloadURL = new DownloadURL();

        Bitmap[] bitmaps = new Bitmap[urls.length];

        for (int i=0; i<bitmaps.length; i++){
            try {
                bitmaps[i] = downloadURL.readUrl(urls[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmaps;
    }

}
