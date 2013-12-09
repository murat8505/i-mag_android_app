package by.imag.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class FragmentListTags extends ListFragment{
    //

    class TagCursorAdapter extends CursorAdapter {

        public TagCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}
