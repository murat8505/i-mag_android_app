package by.imag.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Formatter;
import java.util.List;

import by.imag.app.classes.Constants;
import by.imag.app.classes.TagItem;

public class AppDb extends SQLiteOpenHelper{
    private SQLiteDatabase db;
    public static final String TAGS_TABLE = "tags_table";

    public static final String TAG_NAME = "tagName";
    public static final String TAG_URL = "tagURL";
    public static final String TAG_POSTS = "tagPosts";

    public AppDb(Context context) {
        super(context, "appDb.db", null, 1);
    }

    public boolean writeTagTable(List<TagItem> tags) {
        logMsg("write tag table");
        db = this.getWritableDatabase();
        logMsg("tags: "+tags.size());
        if (tags.size() != 0) {
            db.delete(TAGS_TABLE, null, null);
            ContentValues cvTag = new ContentValues();
            for (TagItem tagItem: tags) {
                String tagName = tagItem.getTagName();
                String tagURL = tagItem.getTagURL();
                int postCount = tagItem.getPostCount();
                cvTag.put(TAG_NAME, tagName);
                cvTag.put(TAG_URL, tagURL);
                cvTag.put(TAG_POSTS, postCount);
                db.insert(TAGS_TABLE, null, cvTag);
            }
        }
        this.close();
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        logMsg("create db");
        String formatString = "create table %s (_id integer primary key autoincrement, " +
                "%s text, %s text, %s integer);";
//        String sqlString = "create table " + TAGS_TABLE;
        Formatter formatter = new Formatter();
        formatter.format(formatString, TAGS_TABLE, TAG_NAME, TAG_URL, TAG_POSTS);
        logMsg("sql: "+formatter.toString());
        String sqlString = formatter.toString();
        db.execSQL(sqlString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }
}
