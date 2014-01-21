package by.imag.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import by.imag.app.classes.ArticlePreview;

public class PostAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<ArticlePreview> posts;

    public PostAdapter(Context context, List<ArticlePreview> posts) {
        this.context = context;
        this.posts = posts;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return posts.get(position).getArticleId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.art_grid_item, parent, false);
        }
        ArticlePreview post = getPost(position);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(post.getArticleTitle());
        ImageView imgArticlePreview = (ImageView) view.findViewById(R.id.imageView);
        String imgUrl = post.getImageURL();
        Picasso.with(context).load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(imgArticlePreview);

        return view;
    }

    private ArticlePreview getPost(int position) {
        return (ArticlePreview) getItem(position);
    }
}