package by.imag.app;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.squareup.picasso.Picasso;

import by.imag.app.classes.ScrollableImageView;

public class AdvActivity extends Activity {
    private ImageView imgAdv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adv_img);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setSubtitle(R.string.price_list);
        imgAdv = (ImageView) findViewById(R.id.imgAdv);
//        imgAdv.setLayoutParams(new LayoutParams(imgAdv.getWidth(), imgAdv.getHeight()));
        imgAdv = new ScrollableImageView(this);
//        Picasso.with(this)
//                .load(R.drawable.adv_2013)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.logo_red)
//                .into(imgAdv);
//        imgAdv.setImageResource(R.drawable.adv_2013);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.adv_2013);
        setImageBitmap(bmp);
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

    private void setImageBitmap(Bitmap bmp) {
        imgAdv = new ScrollableImageView(this);
        imgAdv.setLayoutParams(new LayoutParams(bmp.getWidth(), bmp.getHeight()));
        imgAdv.setImageBitmap(bmp);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(imgAdv);
    }
}
