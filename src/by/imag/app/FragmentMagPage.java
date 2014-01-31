package by.imag.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
//    private ImgLoaderTask imgLoaderTask;
//    private int maxMemory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magId = getArguments().getString(Constants.MAG_ID);
        pageNumber = getArguments().getInt(Constants.MAG_PAGE);
//        maxMemory = (int) ((Runtime.getRuntime().maxMemory())/1024);

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

//        imgLoaderTask = new ImgLoaderTask(imgPage);
//        imgLoaderTask.execute(imgUrl);
        loadImgPicasso();

        return view;
    }

    private void loadImgPicasso() {
        Picasso.with(getActivity().getBaseContext())
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_red)
                .into(imgPage);
    }


//    private Bitmap loadBitmap(String urlStr) {
//        Bitmap bitmap = null;
//        InputStream input = null;
//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            connection.setConnectTimeout(20 * 1000);
//            connection.setReadTimeout(20 * 1000);
//            input = connection.getInputStream();
//            bitmap = BitmapFactory.decodeStream(input);
//            if (input != null) {
//                input.close();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (OutOfMemoryError e) {
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }

//    private class ImgLoaderTask extends AsyncTask<String, Void, Bitmap> {
//        private final WeakReference<TouchImageView> reference;
//
//        public ImgLoaderTask(TouchImageView touchImageView) {
//            reference = new WeakReference<TouchImageView>(touchImageView);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            imgPage.setImageResource(R.drawable.placeholder);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            return loadBitmap(urls[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            if (reference != null && bitmap != null) {
//                imgPage = reference.get();
//                if (imgPage != null) {
//                    imgPage.setImageBitmap(bitmap);
//                }
//            }
//        }
//    }

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
