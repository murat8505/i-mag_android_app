package by.imag.app.json;


import com.google.gson.annotations.SerializedName;

public class Doc {
    private String title;

    @SerializedName("documentId")
    private String id;

    @SerializedName("pagecount")
    private int pageCount;

    @SerializedName("docname")
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
