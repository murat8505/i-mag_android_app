package by.imag.app.classes;


import android.os.Parcel;
import android.os.Parcelable;

public class ArticlePreview implements Parcelable{
    private String articleTitle;
    private String previewText;
    private String articleURL;
    private String imageURL;
    private int articleId;

    public ArticlePreview(String articleTitle, String previewText,
                          String articleURL, String imageURL) {
        this.articleTitle = articleTitle;
        this.previewText = previewText;
        this.articleURL = articleURL;
        this.imageURL = imageURL;
        String[] articleStr = articleURL.split("=");
        this.articleId = Integer.parseInt(articleStr[1]);
    }

    public ArticlePreview(Parcel in) {
        articleTitle = in.readString();
        previewText = in.readString();
        articleURL = in.readString();
        imageURL = in.readString();
        articleId = in.readInt();
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(articleTitle);
        out.writeString(previewText);
        out.writeString(articleURL);
        out.writeString(imageURL);
        out.writeInt(articleId);
    }

    public static final Parcelable.Creator<ArticlePreview> CREATOR =
            new Parcelable.Creator<ArticlePreview>() {
        public ArticlePreview createFromParcel(Parcel in) {
            return new ArticlePreview(in);
        }

        public ArticlePreview[] newArray(int size) {
            return new ArticlePreview[size];
        }
    };

//    @Override
//    public String toString() {
//        return "\n"+
//                articleTitle + "\n" +
//                previewText + "\n" +
//                articleURL + "\n" +
//                imageURL;
//    }
}
