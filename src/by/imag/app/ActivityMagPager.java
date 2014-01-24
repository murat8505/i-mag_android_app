package by.imag.app;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import by.imag.app.classes.Constants;

public class ActivityMagPager extends FragmentActivity {
    private ViewPager viewPager;
    private String magId;
    private String magTitle;
    private String magUrl;
    private int magPostCount = 0;
    private PagerAdapter pagerAdapter;
    private ActionBar actionBar;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mag_pager);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Intent magIntent = getIntent();
        Bundle magBundle = magIntent.getBundleExtra(Constants.MAG_INTENT);
        magId = magBundle.getString(Constants.MAG_ID);
        magTitle = magBundle.getString(Constants.MAG_TITLE);
        magUrl = magBundle.getString(Constants.MAG_URL);
        magPostCount = magBundle.getInt(Constants.MAG_POST_COUNT);
        actionBar.setSubtitle(magTitle);
        viewPager = (ViewPager) findViewById(R.id.magPager);
        pagerAdapter = new MagPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //
            }

            @Override
            public void onPageSelected(int i) {
                //
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //
            }
        });
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

    private class MagPagerAdapter extends FragmentStatePagerAdapter {

        public MagPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position+"";
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentMagPage.newInstance(position, magId);
        }

        @Override
        public int getCount() {
            return magPostCount;
        }
    }
}
