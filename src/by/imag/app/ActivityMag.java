package by.imag.app;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Formatter;

import by.imag.app.classes.Constants;
import by.imag.app.classes.TouchImageView;

public class ActivityMag extends Activity {
    private ShareActionProvider shareActionProvider;
    private final String imgUrlFormat = "http://image.issuu.com/%s/jpg/page_%d.jpg";
    private String magId;
    private String magTitle;
    private String magUrl;
    private int magPostCount = 0;
    private final int startPage = 1;
    private int currentPage = startPage;
    private static final String PAGE_NUMBER = "pageNumber";
    private TouchImageView touchImageView;
    private TextView tvPage;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        logMsg("savedInstanceState: "+savedInstanceState);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE_NUMBER);
        }
        setContentView(R.layout.activity_mag);
//        setContentView(R.layout.activity_mag_scroll);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        logMsg("magTitle: "+magTitle);
        touchImageView = (TouchImageView) findViewById(R.id.imageMagPage);
        tvPage = (TextView) findViewById(R.id.tvPage);
        Intent magIntent = getIntent();
        Bundle magBundle = magIntent.getBundleExtra(Constants.MAG_INTENT);
        magId = magBundle.getString(Constants.MAG_ID);
        magTitle = magBundle.getString(Constants.MAG_TITLE);
        magUrl = magBundle.getString(Constants.MAG_URL);
        magPostCount = magBundle.getInt(Constants.MAG_POST_COUNT);
        actionBar.setSubtitle(magTitle);
        setView(currentPage);
//        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) item.getActionProvider();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, magUrl);
        if (sharingIntent != null) {
            shareActionProvider.setShareIntent(sharingIntent);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_NUMBER, currentPage);
    }

    private void setView(int page) {
        Formatter imgUrlFormatter = new Formatter();
        imgUrlFormatter.format(imgUrlFormat, magId, page);
        String imgUrl = imgUrlFormatter.toString();
        Picasso.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(touchImageView);
        tvPage.setText(currentPage + "/" + magPostCount);
    }

//    private void setView() {
//        ListView lvMagScroll = (ListView) findViewById(R.id.lvMagScroll);
//        List<String> imgUrls = new ArrayList<String>();
//        Formatter imgUrlFormatter = new Formatter();
//        for (int pageNumber = 1; pageNumber <= magPostCount; pageNumber++) {
//            imgUrlFormatter.format(imgUrlFormat, magId, pageNumber);
//            imgUrls.add(imgUrlFormatter.toString());
//        }
//        MagAdapter magAdapter = new MagAdapter(this, imgUrls);
//        lvMagScroll.setAdapter(magAdapter);
//    }

    public void onClickPrev(View view) {
        if (currentPage > startPage) {
            currentPage--;
            setView(currentPage);
        }
    }

    public void onCLickNext(View view) {
        if (currentPage < magPostCount) {
            currentPage++;
            setView(currentPage);
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
