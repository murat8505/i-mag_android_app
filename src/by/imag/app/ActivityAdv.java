package by.imag.app;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import by.imag.app.classes.TouchImageView;

public class ActivityAdv extends Activity {
    private TouchImageView imgAdv;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adv_img);
        imgAdv = (TouchImageView) findViewById(R.id.imgAdv);
        imgAdv.setMaxZoom(3);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setSubtitle(R.string.price_list);
        Picasso.with(this)
                .load(R.drawable.adv_2013)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(imgAdv);
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
}
