package by.imag.app;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import by.imag.app.classes.Constants;
import by.imag.app.classes.DocumentParser;
import by.imag.app.classes.TagItem;

public class FragmentTags extends Fragment {
    private GridView gridView;
    private ProgressBar progressBar;
    private AppDb appDb;
    private Cursor cursor;
//    private TagsCursorAdapter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appDb = new AppDb(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.tags_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridTags);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbTags);
        progressBar.setVisibility(View.GONE);
        new TagsLoader().execute();
//        setView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    private void setView() {
        cursor = appDb.getTagsCursor();
        logMsg("cursor: "+cursor);
        TagsCursorAdapter cursorAdapter = new TagsCursorAdapter(getActivity(), cursor, true);
        logMsg("cursorAdapter: "+cursorAdapter);
        gridView.setAdapter(cursorAdapter);
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // networkInfo.isConnected
            // networkInfo.isConnectedOrConnecting()
            return true;
        }
        return false;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    private class TagsLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean tagsUpdated = false;
            if (isOnline()) {
                DocumentParser documentParser = new DocumentParser(1);
                List<TagItem> tags = documentParser.getTags();
                tagsUpdated = appDb.writeTagTable(tags);
            }
            return tagsUpdated;
        }

        @Override
        protected void onPostExecute(Boolean tagsUpdated) {
            super.onPostExecute(tagsUpdated);
            if (tagsUpdated) {
                setView();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    private class TagsCursorAdapter extends CursorAdapter {

        public TagsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.tag_item, viewGroup, false);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvTagName = (TextView) view.findViewById(R.id.tvTagName);
            TextView tvPostCount = (TextView) view.findViewById(R.id.tvPostCount);

            String tagName = cursor.getString(cursor.getColumnIndex(AppDb.TAG_NAME));
            int postCount = cursor.getInt(cursor.getColumnIndex(AppDb.TAG_POSTS));

            tvTagName.setText(tagName);
            tvPostCount.setText(postCount+"");
        }
    }
}
