package Classes;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadWebContent extends AsyncTask<String, Void, HashMap<String, String>> {

    @Override
    protected HashMap<String, String> doInBackground(String... urls) {

        String webContent = readUrl(urls[0]);

        return extractFeatures(webContent);
    }

    private String readUrl(String s) {

        String data = "";
        InputStream inputStream;
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(s);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bReader.readLine()) != null) {
                builder.append(line);
            }
            data = builder.toString();
            bReader.close();
            inputStream.close();
            urlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public HashMap<String, String> extractFeatures(String result) {

        HashMap<String, String> features = new HashMap<>();

        Pattern p = Pattern.compile("img src=\"(.*?)\"");
        Matcher m = p.matcher(result);

        List<String> imgUrl = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<String> condition = new ArrayList<>();
        List<String> time = new ArrayList<>();

        while (m.find()) {
            imgUrl.add(m.group(1));
        }

        p = Pattern.compile("<h5 class=\"card-title\">(.*?)</h5>");
        m = p.matcher(result);

        while (m.find()) {
            name.add(m.group(1));
        }

        p = Pattern.compile("<p class=\"card-text\" id=\"a_condition\">(.*?)</p>");
        m = p.matcher(result);

        while (m.find()) {
            condition.add(0, m.group(1));
        }

        p = Pattern.compile("<p class=\"card-text\" id=\"p_condition\">(.*?)</p>");
        m = p.matcher(result);

        while (m.find()) {
            condition.add(1, m.group(1));
        }

        p = Pattern.compile("<p class=\"card-text\" id=\"o_condition\">(.*?)</p>");
        m = p.matcher(result);

        while (m.find()) {
            condition.add(2, m.group(1));
        }

        p = Pattern.compile("<small class=\"text-muted\">(.*?)</small></p>");
        m = p.matcher(result);

        while (m.find()) {
            time.add(m.group(1));
        }

        features.put("a_name", name.get(0));
        features.put("a_condition", condition.get(0));
        features.put("a_time", time.get(0));
        features.put("a_url", imgUrl.get(0));

        features.put("b_name", name.get(1));
        features.put("b_condition", condition.get(1));
        features.put("b_time", time.get(1));
        features.put("b_url", imgUrl.get(1));

        features.put("o_name", name.get(2));
        features.put("o_condition", condition.get(2));
        features.put("o_time", time.get(2));
        features.put("o_url", imgUrl.get(2));

        return features;
    }
}
