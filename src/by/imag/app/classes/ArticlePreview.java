package by.imag.app.classes;


import android.graphics.Bitmap;

public class ArticlePreview {
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

//    @Override
//    public String toString() {
//        return "\n"+
//                articleTitle + "\n" +
//                previewText + "\n" +
//                articleURL + "\n" +
//                imageURL;
//    }
}
