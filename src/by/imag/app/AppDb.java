package by.imag.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Formatter;
import java.util.List;

import by.imag.app.classes.ArticlePreview;
import by.imag.app.classes.Constants;
import by.imag.app.classes.TagItem;

public class AppDb extends SQLiteOpenHelper{
    private SQLiteDatabase db;
    public static final String TAGS_TABLE = "tags_table";
    public static final String ARCHIVES_TABLE = "archivesTable";
    public static final String ARTICLES_TABLE = "articles_table";

    public static final String TAG_NAME = "tagName";
    public static final String TAG_URL = "tagURL";
    public static final String TAG_POSTS = "tagPosts";

    public static final String ARCH_NAME = "archName";
    public static final String ARCH_URL = "archUrl";
    public static final String ARCH_ID = "archId";

    public static final String ARTICLE_TITLE = "articleTitle";
    public static final String ARTICLE_TEXT = "articleText";
    public static final String ARTICLE_URL = "articleUrl";
    public static final String ARTICLE_IMAGE_URL = "articleImageUrl";
    public static final String ARTICLE_ID = "articleId";

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

    public Cursor getTagsCursor() {
        logMsg("get tags cursor");
        db = this.getReadableDatabase();
        return db != null ? db.query(TAGS_TABLE, null, null, null, null, null, null) : null;
    }

    public boolean writeArticlesTable(List<ArticlePreview> articles) {
        logMsg("write articles table");
        db = this.getWritableDatabase();
        if (articles.size() != 0) {
//            db.delete(ARTICLES_TABLE, null, null);
            ContentValues cvArticles = new ContentValues();
            for (ArticlePreview article: articles) {
                String articleTitle = article.getArticleTitle();
                String previewText = article.getPreviewText();
                String articleURL = article.getArticleURL();
                String[] articleStr = articleURL.split("=");
                int articleId = Integer.parseInt(articleStr[1]);
//                logMsg("url: " + articleURL);
                String imageURL = article.getImageURL();
                String sql = "SELECT * FROM " + ARTICLES_TABLE + " WHERE " + ARTICLE_URL +
                        " = " + "'" + articleURL + "'";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.moveToFirst()) {
//                    logMsg("record exist");
                } else {
//                    logMsg("write record");
                    cvArticles.put(ARTICLE_TITLE, articleTitle);
                    cvArticles.put(ARTICLE_TEXT, previewText);
                    cvArticles.put(ARTICLE_URL, articleURL);
                    cvArticles.put(ARTICLE_IMAGE_URL, imageURL);
                    cvArticles.put(ARTICLE_ID, articleId);
                    db.insert(ARTICLES_TABLE, null, cvArticles);
                }
//                String[] columns = {ARTICLE_URL};
//                String selection = ARTICLE_URL + " = " + "\"" + articleURL + "\"";
//                Cursor cursor = db.query(ARTICLES_TABLE, columns, selection,
//                        null, null, null, null);
//                int index = cursor.getColumnIndexOrThrow(ARTICLE_URL);
//                logMsg("index: "+index);
//                logMsg("url: "+cursor.getString(index));
            }
        }
        this.close();
        return true;
    }

//    private boolean verify(String articleUrl) {
//        int count = -1;
//        Cursor cursor = null;
//        String query = "SELECT COUNT(*) FROM " + ARTICLES_TABLE + " WHERE " +
//                ARTICLE_URL + " = ?";
//        cursor = db.rawQuery(query, new String[] {articleUrl});
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0);
//        }
//        return count > 0;
//    }

    public Cursor getArticlesCursor() {
        logMsg("get articles cursor");
        db = this.getReadableDatabase();
//        return db != null ? db.query(ARTICLES_TABLE, null, null, null, null, null, null) : null;
        String sqlQuery = "SELECT * FROM " + ARTICLES_TABLE + " ORDER BY " + ARTICLE_ID + " DESC";
        return db != null ? db.rawQuery(sqlQuery, null) : null;
    }

    public Cursor getArticlesCursor(int limit, int offset) {
        db = this.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + ARTICLES_TABLE + " ORDER BY " + ARTICLE_ID + " DESC" +
                " LIMIT " + limit + " OFFSET " + offset;
        return db != null ? db.rawQuery(sqlQuery, null) : null;
    }

    public Cursor getArticlesCursor(int pageNumber) {
        db = this.getReadableDatabase();
        int artCount = Constants.ARTICLES_ON_PAGE;
        int offset = pageNumber*artCount-(artCount); //artCount+1
        String sqlQuery = "SELECT * FROM " + ARTICLES_TABLE + " ORDER BY " + ARTICLE_ID + " DESC" +
                " LIMIT " + artCount + " OFFSET " + offset;
        return db != null ? db.rawQuery(sqlQuery, null) : null;
    }

    public ArticlePreview getArticlePreview(long _id) {
        ArticlePreview articlePreview = null;
        db = this.getReadableDatabase();
        String table = ARTICLES_TABLE;
        String[] columns = {ARTICLE_TITLE, ARTICLE_TEXT, ARTICLE_URL, ARTICLE_IMAGE_URL};
        String selection = "_id" + " = " + _id;
        Cursor cursor = db.query(table, columns, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            String articleTitle = cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE));
            String previewText = cursor.getString(cursor.getColumnIndex(ARTICLE_TEXT));
            String articleURL = cursor.getString(cursor.getColumnIndex(ARTICLE_URL));
            String imageURL = cursor.getString(cursor.getColumnIndex(ARTICLE_IMAGE_URL));
            articlePreview = new ArticlePreview(articleTitle, previewText, articleURL,imageURL);
        }
        this.close();
        return articlePreview;
    }

    public TagItem getTagItem(long _id) {
        TagItem tagItem = null;
        db = this.getReadableDatabase();
        String table = TAGS_TABLE;
        String[] columns = {TAG_NAME, TAG_URL, TAG_POSTS};
        String selection = "_id" + " = " + _id;
        Cursor cursor = db.query(table, columns, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            String tagName = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            String tagUrl = cursor.getString(cursor.getColumnIndex(TAG_URL));
            int postCount = cursor.getInt(cursor.getColumnIndex(TAG_POSTS));
            tagItem = new TagItem(tagName, tagUrl, postCount);
        }
        this.close();
        return tagItem;
    }

    public String getTagUrl(long _id) {
        String tagUrl = "";
        db = this.getReadableDatabase();
        String table = TAGS_TABLE;
        String[] columns = {TAG_URL};
        String selection = "_id" + " = " + _id;
        Cursor cursor = db.query(table, columns, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            tagUrl = cursor.getString(cursor.getColumnIndex(TAG_URL));
        }
        this.close();
        return tagUrl;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        logMsg("create db");
        String formatString = "create table %s (_id integer primary key autoincrement, " +
                "%s text, %s text, %s integer);";
//        String sqlString = "create table " + TAGS_TABLE;
        Formatter formatter = new Formatter();
        formatter.format(formatString, TAGS_TABLE, TAG_NAME, TAG_URL, TAG_POSTS);
//        logMsg("sql: "+formatter.toString());
        String sqlString = formatter.toString();
        db.execSQL(sqlString);

        //create table ARTICLES_TABLE (_id integer primary key autoincrement,
        // )
        formatString = "create table %s (_id integer primary key autoincrement, " +
                "%s text, %s text, %s text, %s text, %s integer);";
        Formatter artFormatter = new Formatter();
        artFormatter.format(formatString, ARTICLES_TABLE, ARTICLE_TITLE, ARTICLE_TEXT, ARTICLE_URL,
                ARTICLE_IMAGE_URL, ARTICLE_ID);
//        logMsg("sql: "+artFormatter.toString());
        sqlString = artFormatter.toString();
        db.execSQL(sqlString);

        formatString = "create table %s (_id integer primary key autoincrement, " +
                "%s text, %s text, %s integer);";
        Formatter archFormatter = new Formatter();
        archFormatter.format(formatString, ARCHIVES_TABLE, ARCH_NAME, ARCH_URL, ARCH_ID);
        sqlString = archFormatter.toString();
        db.execSQL(sqlString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }
}
