package by.imag.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ArticleAdapter extends BaseAdapter{
    private Context context;
    private String imgUrl;
    private String articleTitle;

    public ArticleAdapter(Context context, String imgUrl, String articleTitle) {
        this.context = context;
        this.imgUrl = imgUrl;
        this.articleTitle = articleTitle;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.art_grid_item, null);
            ImageView img = (ImageView) gridView.findViewById(R.id.imageView);
            TextView tvTitle = (TextView) gridView.findViewById(R.id.tvTitle);
            tvTitle.setText(articleTitle);
            Picasso.with(context).load(imgUrl).error(R.drawable.ic_launcher).into(img);

        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }
}
