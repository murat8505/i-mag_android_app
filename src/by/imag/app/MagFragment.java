package by.imag.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.Constants;
import by.imag.app.classes.HtmlParserThread;
import by.imag.app.classes.MagItem;
import by.imag.app.json.Doc;
import by.imag.app.json.OverResponse;
import by.imag.app.json.Response;

public class MagFragment extends Fragment{
    //http://search.issuu.com/api/2_0/document?q=username:vovic2000&sortBy=epoch&pageSize=6
    //http://search.issuu.com/api/2_0/document?q=documentId:140109103402-1931c53b51bfbd91c262c9e0f3308319&responseParams=*

    //http://search.issuu.com/api/2_0/document?q=username:vovic2000&sortBy=epoch&pageSize=6&responseParams=*

    private final String magPageUrl = "http://i-mag.by/?page_id=641";
    private final String issuuUrl = "http://search.issuu.com/api/2_0/" +
            "document?q=username:vovic2000&sortBy=epoch&pageSize=";
    private int magCount;
    private AppDb appDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mag_frag, container, false);
        appDb = new AppDb(getActivity().getApplicationContext());
//        new TestParser().execute(magPageUrl);
        new MagLoader().execute();
        return rootView;
    }

    //todo: isOnline

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    private class MagLoader extends AsyncTask<Void, Void, Boolean> {
        //todo: remake

        @Override
        protected Boolean doInBackground(Void... params) {
            Document document = null;
            boolean result = false;
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Document> documentFuture = executorService.submit(
                    new HtmlParserThread(magPageUrl));
            try {
                document = documentFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            if (document != null) {
                Elements elements = document.select(".issuuembed");
                logMsg("elements: "+elements.size());
                if (elements.size() > 0) {
                    logMsg("text: "+elements.get(0).toString());
                    magCount = elements.size();
                }
            } else {
                logMsg("document: "+document);
            }

            try {
                String magUrl = issuuUrl + magCount;
                URL url = new URL(magUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Gson gson = new Gson();
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                    logMsg("stream:"+inputStreamReader);
//                    Response response = gson.fromJson(inputStreamReader, Response.class);
                    OverResponse overResponse = gson.fromJson(inputStreamReader, OverResponse.class);
//                    logMsg("over response: "+overResponse);
                    Response response = overResponse.getResponse();
//                    logMsg("response: "+response);
                    ArrayList<Doc> docs = response.getDocs();
//                    logMsg("docs: "+docs);
                    ArrayList<MagItem> magItems = new ArrayList<MagItem>();
                    for (Doc doc: docs) {
                        MagItem magItem = new MagItem(doc.getDocumentId(), doc.getDocname());
                        magItems.add(magItem);
                    }
                    result = appDb.writeMagTable(magItems);
                    logMsg("result: "+result);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private class TestParser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Document document = null;
            String result = "";
            String url = strings[0];
            logMsg("url: "+url);
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Document> documentFuture = executorService.submit(
                    new HtmlParserThread(url));
            try {
                document = documentFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            if (document != null) {
                Elements elements = document.select(".issuuembed");
                logMsg("elements: "+elements.size());
                if (elements.size() > 0) {
                    logMsg("text: "+elements.get(0).toString());
                }
            } else {
                logMsg("document: "+document);
            }
            return result;
        }
    }
}
