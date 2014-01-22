package by.imag.app;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {
    private Context context;
    private String[] menuItemsArray;
    private LayoutInflater inflater;

    public DrawerAdapter(Context context, String[] menuItemsArray) {
        this.context = context;
        this.menuItemsArray = menuItemsArray;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuItemsArray.length;
    }

    @Override
    public Object getItem(int position) {
        return menuItemsArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.drawer_item, viewGroup, false);
        }
        String fontName = "fonts/Roboto-Regular.ttf";
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
        String text = getText(position);
        TextView tvDrawerItem = (TextView) view.findViewById(R.id.tvDrawerItem);
        tvDrawerItem.setText(text);
        tvDrawerItem.setTypeface(typeface);
        float fontSize = context.getResources().getDimension(R.dimen.drawer_item_text_size);
        tvDrawerItem.setTextSize(fontSize);
        return view;
    }

    private String getText(int position) {
        return context.getResources().getStringArray(R.array.menu_items)[position];
    }
}
