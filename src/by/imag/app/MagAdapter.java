package by.imag.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import by.imag.app.classes.TouchImageView;

public class MagAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    List<String> imgUrls;

    public MagAdapter(Context context, List<String> imgUrls) {
        this.context = context;
        this.imgUrls = imgUrls;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imgUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.mag_scroll_item, viewGroup, false);
        }
        TouchImageView touchImageView = (TouchImageView) view.findViewById(R.id.magScrollItem);
        String imgUrl = (String) getItem(position);
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(touchImageView);
        return view;
    }
}
