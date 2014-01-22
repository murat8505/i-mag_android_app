package by.imag.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
import by.imag.app.json.JsonResponse;
import by.imag.app.json.Response;

public class FragmentMag extends BaseFragment{
    //http://search.issuu.com/api/2_0/document?q=username:vovic2000&sortBy=epoch&pageSize=6
    //http://search.issuu.com/api/2_0/document?q=documentId:140109103402-1931c53b51bfbd91c262c9e0f3308319&responseParams=*

    //http://search.issuu.com/api/2_0/document?q=username:vovic2000&sortBy=epoch&pageSize=6&responseParams=*

    private final String magPageUrl = "http://i-mag.by/?page_id=641";
    private final String issuuUrl = "http://search.issuu.com/api/2_0/" +
            "document?q=username:vovic2000&sortBy=epoch&pageSize=";
    private int magCount;
    private boolean update;
    private AppDb appDb;
    private SharedPreferences preferences;
    private GridView gridView;
    private ProgressBar pbMag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mag, container, false);
        appDb = new AppDb(getActivity().getApplicationContext());
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        gridView = (GridView) rootView.findViewById(R.id.gridMag);
        pbMag = (ProgressBar) rootView.findViewById(R.id.pbMag);
        loadPreferences();
        logMsg("update: "+update);
        if (isOnline() && update) {
            new MagListLoader().execute();
        }
        setActionBarSubtitle(6);
//        setView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    private void setView() {
        Cursor cursor = appDb.getMagCursor();
        MagCursorAdapter magCursorAdapter = new MagCursorAdapter(getActivity(), cursor, true);
        gridView.setAdapter(magCursorAdapter);
        gridView.setNumColumns(getNumColumns());
        onMagClick();
    }

    private void onMagClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long _id) {
                MagItem magItem = appDb.getMagItem(_id);
//                logMsg("magItem: "+magItem);
                Intent magIntent = new Intent(getActivity(), ActivityMag.class);
//                Intent magIntent = new Intent(getActivity(), TestActivity.class);
                Bundle magBundle = new Bundle();
                magBundle.putString(Constants.MAG_ID, magItem.getMagId());
                String magTitle = magItem.getMagTitle();
//                logMsg("magTitle: " + magTitle);
                magTitle = magTitle.replace("Журнал \"Я\" ", "");
//                logMsg("magTitle: "+magTitle);
                magBundle.putString(Constants.MAG_TITLE, magTitle);
                magBundle.putString(Constants.MAG_URL, magItem.getMagUrl());
                magBundle.putInt(Constants.MAG_POST_COUNT, magItem.getMagPageCount());
                magIntent.putExtra(Constants.MAG_INTENT, magBundle);
                startActivity(magIntent);
            }
        });
    }

    @SuppressWarnings("deprecation")
    private int getNumColumns() {
        float gridSize = getResources().getDimension(R.dimen.mag_item_width);
        int number = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / gridSize);
        return columns;
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.UPDATE_MAGS, false);
        editor.commit();
    }

    private void loadPreferences() {
        update = preferences.getBoolean(Constants.UPDATE_MAGS, true);
    }

//    private boolean isOnline() {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            // networkInfo.isConnected
//            // networkInfo.isConnectedOrConnecting()
//            return true;
//        }
//        return false;
//    }
//
//    private void logMsg(String msg) {
//        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
//    }

    private class MagListLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbMag.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            Document document = null;
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
                if (elements.size() > 0) {
                    logMsg("text: "+elements.get(0).toString());
                    magCount = elements.size();
                }
            }

            String magUrl = "http://search.issuu.com/api/2_0/document?" +
                    "q=username:vovic2000&sortBy=epoch&pageSize=" + magCount + "&responseParams=*";
            try {
                URL url = new URL(magUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Gson gson = new Gson();
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                    logMsg("stream:"+inputStreamReader);
                    JsonResponse jsonResponse = gson.fromJson(inputStreamReader, JsonResponse.class);
                    logMsg("jsonResponse: "+jsonResponse);
                    Response response = jsonResponse.getResponse();
                    logMsg("response: "+response);
                    ArrayList<Doc> docs = response.getDocs();
                    logMsg("docs: "+docs);
                    ArrayList<MagItem> magItems = new ArrayList<MagItem>();
                    for (Doc d: docs) {
                        MagItem magItem = new MagItem(
                                d.getId(),
                                d.getTitle(),
                                d.getUrl(),
                                d.getPageCount()
                        );
                        magItems.add(magItem);
                    }
                    result = appDb.writeMagTable(magItems);
                    connection.disconnect();
//                    logMsg("result: "+result);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            logMsg("result: "+result);
            if (result) {
                if (isAdded()) {
                    setView();
                }
                savePreferences();
            }
            pbMag.setVisibility(View.GONE);
        }
    }

    private class MagCursorAdapter extends CursorAdapter {

        public MagCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.mag_grid_item, viewGroup, false);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imgMag);
            String imageUrl = cursor.getString(cursor.getColumnIndex(AppDb.MAG_IMG_URL));
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.placeholder)
                    .error(R.drawable.logo_red)
                    .into(imageView);
        }
    }
}
