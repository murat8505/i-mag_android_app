package by.imag.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Formatter;

import by.imag.app.classes.Constants;
import by.imag.app.classes.TouchImageView;

public class FragmentMagPage extends Fragment {
    private TouchImageView imgPage;
    private final String imgUrlFormat = "http://image.issuu.com/%s/jpg/page_%d.jpg";
    private int pageNumber;
    private String magId;
    private String imgUrl;
    private ImgLoader imgLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magId = getArguments().getString(Constants.MAG_ID);
        pageNumber = getArguments().getInt(Constants.MAG_PAGE);
        imgLoader = new ImgLoader();
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
//        imgLoader.execute(imgUrl);
        loadImgPicasso();
//        loadImgImageLoader();

        return view;
    }

    private void loadImgPicasso() {
        Picasso.with(getActivity().getBaseContext())
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(imgPage);
    }

    private void loadImgImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getBaseContext())
                .memoryCacheExtraOptions(1109, 1496)
                .threadPoolSize(6)
                .threadPriority(Thread.MIN_PRIORITY + 3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageOnFail(R.drawable.logo_red)
                .build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage(imgUrl, imgPage, options);
    }

    private Bitmap loadBitmap(String urlStr) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private class ImgLoader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgPage.setImageResource(R.drawable.placeholder);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            return loadBitmap(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgPage.setImageBitmap(bitmap);
        }
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
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
