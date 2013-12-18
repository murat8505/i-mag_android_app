package by.imag.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import by.imag.app.classes.Constants;

public class TestFragment extends Fragment {
    private BroadcastReceiver broadcastReceiver;
    private boolean isRegistered = false;
    private AppDb appDb;
    private ListView lvArts;
    private SimpleCursorAdapter simpleCursorAdapter;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
//        appDb = new AppDb(getActivity().getApplicationContext());
//        lvArts = (ListView) rootView.findViewById(R.id.lvTestArt);
//        receiveData();
//        setView();
//        return rootView;
//
//    }
//
//    private void receiveData() {
//        logMsg("receive data");
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                boolean isUpdated = false;
//                isUpdated = intent.getBooleanExtra(Constants.INTENT_ARTICLES, false);
//                if (isUpdated) {
//                    if (isAdded()) {
//                        setView();
//                    }
//                }
//            }
//        };
//        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
//        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
//        isRegistered = true;
//    }
//
//    protected void unregister() {
//        if (isRegistered) {
//            getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
//            isRegistered = false;
//        }
//    }
//
//    private void setView() {
//        //
//    }
//
//    private void logMsg(String msg) {
//        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
//    }
//
//    class TestCursorAdapter extends CursorAdapter {
//
//        public TestCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
//            super(context, cursor, autoRequery);
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//            LayoutInflater layoutInflater = LayoutInflater.from(context);
//            View view = layoutInflater.inflate(R.layout.article_list_item, viewGroup, false);
//            bindView(view, context, cursor);
//            return view;
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            ImageView imageView = (ImageView) view.findViewById(R.id.imgArticlePreview);
//            TextView tvArticleTitle = (TextView) view.findViewById(R.id.tvArticleTitle);
//            TextView tvArticleText = (TextView) view.findViewById(R.id.tvArticleText);
//
//            String imgUrl = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_IMAGE_URL));
//            String articleTitle = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TITLE));
//            String articleText = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TEXT));
//
//            tvArticleTitle.setText(articleTitle);
//            tvArticleText.setText(articleText);
//        }
//    }
}
