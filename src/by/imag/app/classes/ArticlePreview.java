package by.imag.app.classes;


import java.net.URL;
import java.util.ArrayList;

public class ArticlePreview {
    private String articleTitle;
    private String previewText;
    private String articleURL;
    private String imageURL;

    public ArticlePreview(String articleTitle, String previewText,
                          String articleURL, String imageURL) {
        this.articleTitle = articleTitle;
        this.previewText = previewText;
        this.articleURL = articleURL;
        this.imageURL = imageURL;
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
}
