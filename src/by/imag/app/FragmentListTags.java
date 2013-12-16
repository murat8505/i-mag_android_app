package by.imag.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;//
import android.support.v4.app.LoaderManager;//
import android.support.v4.content.CursorLoader;//
import android.support.v4.content.Loader;//
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.CursorAdapter;

import by.imag.app.classes.Constants;

public class FragmentListTags extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private AppDb appDb;
    private SimpleCursorAdapter simpleCursorAdapter;
    private BroadcastReceiver broadcastReceiver;
    private boolean isRegistered;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
        receiveData();
    }

    private void setView() {
        logMsg("setView");
        String[] from = new String[] {AppDb.TAG_NAME, AppDb.TAG_POSTS};
        int[] to = new int[] {R.id.tvTagName, R.id.tvPostCount};
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.tag_item, null,
                from, to ,0);
        setListAdapter(simpleCursorAdapter);
//        getLoaderManager().initLoader(0, null, this);
//        getSupportLoaderManager()
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        appDb = new AppDb(getActivity().getApplicationContext());
        setView();
        receiveData();
        return inflater.inflate(R.layout.tags_list, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new TagsCursorLoader(getActivity().getApplicationContext(), appDb);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void receiveData() {
        logMsg("receive data");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isUpdated = false;
                isUpdated = intent.getBooleanExtra(Constants.INTENT_TAGS, false);
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

    static class TagsCursorLoader extends CursorLoader {
        AppDb appDb;

        public TagsCursorLoader(Context context, AppDb appDb) {
            super(context);
            this.appDb = appDb;
        }

        @Override
        public Cursor loadInBackground() {
//            Log.d(Constants.LOG_TAG, getClass().getSimpleName()+": appDb: "+appDb);
            Cursor cursor = appDb.getTagsCursor();
//            Log.d(Constants.LOG_TAG, getClass().getSimpleName()+"cursor: "+cursor);
            return cursor;
        }
    }
}
