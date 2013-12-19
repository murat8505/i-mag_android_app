package by.imag.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.BitmapLoader;
import by.imag.app.classes.Constants;

public class FragmentListArticles extends Fragment {
    private AppDb appDb;
    private ListView listView;
    private boolean isRegistered = false;
    private BroadcastReceiver broadcastReceiver;
    private AppService appService;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        appDb = new AppDb(getActivity());
        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvTestArt);

        receiveData();
        setView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregister();
    }

    private void setView() {
        Cursor cursor = appDb.getArticlesCursor();
        ArticleCursorAdapter cursorAdapter = new ArticleCursorAdapter(
                getActivity(), cursor, true);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                logMsg("item click: " + position + ", id: " + id);
            }
        });
    }

//    public void  onClickNext(View view) {
//        switch (view.getId()) {
//            case R.id.btnNext:
//                appService.parseNext();
//            break;
//            default: logMsg("error");
//        }
//    }

    private void receiveData() {
        logMsg("receive data");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isUpdated = false;
                isUpdated = intent.getBooleanExtra(Constants.INTENT_ARTICLES, false);
                if (isUpdated) {
                    if (isAdded()) {
                        setView();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
        isRegistered = true;
    }

    protected void unregister() {
        if (isRegistered) {
            getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
            isRegistered = false;
        }
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }

    class ArticleCursorAdapter extends CursorAdapter {

        public ArticleCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
            super(context, cursor, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.article_list_item, viewGroup, false);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imgArticlePreview = (ImageView) view.findViewById(R.id.imgArticlePreview);
            TextView tvArticleTitle = (TextView) view.findViewById(R.id.tvArticleTitle);
            TextView tvArticleText = (TextView) view.findViewById(R.id.tvArticleText);

            String imgUrl = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_IMAGE_URL));
            String articleTitle = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TITLE));
            String articleText = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TEXT));

//            ExecutorService executorService = Executors.newFixedThreadPool(1);
//            Future<Bitmap> bitmapFuture = executorService.submit(new BitmapLoader(imgUrl));
//            Bitmap img = null;
//            try {
//                img = bitmapFuture.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            executorService.shutdown();

//            ImageSetter imageSetter = new ImageSetter();
//            Bitmap img = null;
//            try {
//                img = imageSetter.execute(imgUrl).get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            imgArticlePreview.setImageBitmap(img);

            Picasso.with(getActivity().getApplicationContext()).load(imgUrl)
                    .error(R.drawable.ic_launcher)
                    .into(imgArticlePreview);
//            Picasso.with(getActivity().getApplicationContext()).setDebugging(true);
            tvArticleTitle.setText(articleTitle);
            tvArticleText.setText(articleText);
        }
    }

    class ImageSetter extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imgUrl = strings[0];
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Bitmap> bitmapFuture = executorService.submit(new BitmapLoader(imgUrl));
            Bitmap bitmap = null;
            try {
                bitmap = bitmapFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }
}
