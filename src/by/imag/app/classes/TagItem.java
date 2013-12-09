package by.imag.app.classes;

public class TagItem {
    private String tagName;
    private String tagURL;
    private int postCount;

    public TagItem(String tagName, String tagURL, int postCount) {
        this.tagName = tagName;
        this.tagURL = tagURL;
        this.postCount = postCount;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagURL() {
        return tagURL;
    }

    public void setTagURL(String tagURL) {
        this.tagURL = tagURL;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
}
