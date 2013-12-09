package by.imag.app;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import by.imag.app.classes.Constants;


public class MainActivity extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ServiceConnection sConn;
    private AppService appService;
    private boolean isServiceBound = false;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] menuTitles;

    @Override
    protected void onStart() {
        super.onStart();
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                appService = ((AppService.ServiceBinder) iBinder).getService();
                isServiceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isServiceBound = false;
            }
        };
        Intent serviceIntent = new Intent(this, AppService.class);
        startService(serviceIntent);
        bindService(serviceIntent, sConn, 0);

    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        title = drawerTitle = getTitle();
        menuTitles = getResources().getStringArray(R.array.menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(title);
            }

            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
	}

    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        FragmentManager fragmentManager = getFragmentManager();
//        setTitle(menuTitles[position]);
        logMsg("position: "+position);
        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new FragmentListArticles()).commit();
                getActionBar().setSubtitle(menuTitles[position]);
            break;
            default: getActionBar().setSubtitle(menuTitles[position]);
        }
        getActionBar().setSubtitle(menuTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

}
