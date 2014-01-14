package by.imag.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
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
    private SharedPreferences preferences;
    private boolean update;
//    private TagsCursorAdapter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appDb = new AppDb(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.tags_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridTags);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbTags);
        progressBar.setVisibility(View.GONE);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        loadPreferences();
        new TagsLoader().execute();

        getActivity().getActionBar().setSubtitle("Рубрики");
//        setView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    private void loadPreferences() {
        update = preferences.getBoolean(Constants.UPDATE_TAGS, true);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.UPDATE_TAGS, false);
        editor.commit();
    }

    @SuppressWarnings("deprecation")
    private int getNumColumns() {
        float gridSize = getResources().getDimension(R.dimen.tag_size);
        int number = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / gridSize);
        return columns;
    }

    private void setView() {
        cursor = appDb.getTagsCursor();
//        logMsg("cursor: "+cursor);
        TagsCursorAdapter cursorAdapter = new TagsCursorAdapter(getActivity(), cursor, true);
//        logMsg("cursorAdapter: "+cursorAdapter);
        gridView.setAdapter(cursorAdapter);
        gridView.setNumColumns(getNumColumns());
        tagClick();
    }

    private void tagClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long _id) {
                TagItem tagItem = appDb.getTagItem(_id);
//                String tagUrl = tagItem.getTagURL();
//                String tagName = tagItem.getTagName();
//                logMsg("tag URL: "+tagUrl);
                Fragment testFragment = new PostsFragment(tagItem);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.content_frame, testFragment);
//                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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
            if (isOnline() && update) {
                DocumentParser documentParser = new DocumentParser(Constants.PAGE + 1);
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
                savePreferences();
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
