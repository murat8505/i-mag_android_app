package by.imag.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.Constants;
import by.imag.app.classes.HtmlParserThread;

public class MagFragment extends Fragment{
    String magPageUrl = "http://issuu.com/vovic2000";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mag_frag, container, false);
        new TestParser().execute(magPageUrl);
        return rootView;
    }

    //todo: isOnline

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
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
                Elements elements = document.select("div[id=main-container]");
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
