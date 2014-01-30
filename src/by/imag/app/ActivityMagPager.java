package by.imag.app;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Formatter;

import by.imag.app.classes.Constants;
import by.imag.app.classes.TouchImageView;

public class ActivityMagPager extends FragmentActivity {
    private final String imgUrlFormat = "http://image.issuu.com/%s/jpg/page_%d.jpg";
    private ViewPager viewPager;
    private String magId;
    private String magTitle;
    private String magUrl;
    private int magPostCount = 0;
    private MagPagerAdapter magPagerAdapter;
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
        magPagerAdapter = new MagPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(magPagerAdapter);
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
        getMenuInflater().inflate(R.menu.menu_mag, menu);
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
//            case R.id.action_go_to:
//
//                goToPage();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void goToPage() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View dialogView = inflater.inflate(R.layout.dialog_go_to, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView);
//        final EditText etGoTo = (EditText) dialogView.findViewById(R.id.etGoTo);
//        builder.setCancelable(false).setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        int pageNumber = Integer.parseInt(String.valueOf(etGoTo.getText()));
//                        logMsg("page: "+pageNumber);
//                        viewPager.setCurrentItem(pageNumber);
//                    }
//                }).setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }


    private class MagPagerAdapter extends FragmentStatePagerAdapter {

        public MagPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            object = null;
//            logMsg("destroy item: "+position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position+1+"";
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentMagPage.newInstance(position+1, magId);
        }

        @Override
        public int getCount() {
            return magPostCount;
        }
    }
}
