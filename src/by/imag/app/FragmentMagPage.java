package by.imag.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.Formatter;

import by.imag.app.classes.Constants;
import by.imag.app.classes.TouchImageView;

public class FragmentMagPage extends Fragment {
    private TouchImageView imgPage;
    private final String imgUrlFormat = "http://image.issuu.com/%s/jpg/page_%d.jpg";
    private int pageNumber;
    private String magId;
    private String imgUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magId = getArguments().getString(Constants.MAG_ID);
        pageNumber = getArguments().getInt(Constants.MAG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mag_page, null);
        imgPage = (TouchImageView) view.findViewById(R.id.touchMagPage);
        imgPage.setMaxZoom(3);
        Formatter imgUrlFormatter = new Formatter();
        imgUrlFormatter.format(imgUrlFormat, magId, pageNumber);
        imgUrl = imgUrlFormatter.toString();
        Picasso.with(getActivity().getBaseContext())
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(imgPage);

//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
//        imageLoader.displayImage(imgUrl, imgPage);
        return view;
    }

    static FragmentMagPage newInstance(int pageNumber, String magId) {
        FragmentMagPage fragmentMagPage = new FragmentMagPage();
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.MAG_PAGE, pageNumber);
        arguments.putString(Constants.MAG_ID, magId);
        fragmentMagPage.setArguments(arguments);
        return fragmentMagPage;
    }
}
