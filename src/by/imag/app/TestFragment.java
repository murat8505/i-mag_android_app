package by.imag.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import by.imag.app.classes.Constants;

public class TestFragment extends Fragment {
    private BroadcastReceiver broadcastReceiver;
    private boolean isRegistered = false;
    private AppDb appDb;
    private ListView lvTags;

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        logMsg("onCreateView");
//        appDb = new AppDb(getActivity().getApplicationContext());
//        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
//        lvTags = (ListView) rootView.findViewById(R.id.lvTags);
//        return rootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setView();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregister();
//    }
//
//    private void setView() {
//        logMsg("setView");
//        Cursor cursor = appDb.getTagsCursor();
//        TestCursorAdapter testCursorAdapter = new TestCursorAdapter(
//                getActivity().getApplicationContext(), cursor, true);
//        lvTags.setAdapter(testCursorAdapter);
//    }
//
//    private void receiveData() {
//        logMsg("receive data");
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                boolean isUpdated = false;
//                isUpdated = intent.getBooleanExtra(Constants.INTENT_TAGS, false);
//                if (isUpdated) {
//                    if (isAdded()) {
//                        setView();
//                    }
//                }
//            }
//
//        };
//        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
//        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
//        isRegistered = true;
//    }
//
//    private void unregister() {
//        if (isRegistered) {
//            getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
//            isRegistered = false;
//        }
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
//            View view = layoutInflater.inflate(R.layout.tag_item, viewGroup, false);
//            bindView(view, context, cursor);
//            return view;
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            logMsg("bindView");
//            TextView tvTagName = (TextView) view.findViewById(R.id.tvTagName);
//            TextView tvPostCount = (TextView) view.findViewById(R.id.tvPostCount);
//
//            String tagName = cursor.getString(cursor.getColumnIndex(AppDb.TAG_NAME));
//            int postCount = cursor.getInt(cursor.getColumnIndex(AppDb.TAG_POSTS));
//
//            logMsg(tagName + " " + postCount);
//
//            tvTagName.setText(tagName);
//            tvPostCount.setText(postCount+"");
//        }
//    }
}
