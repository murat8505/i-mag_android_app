package by.imag.app.json;


import com.google.gson.annotations.SerializedName;

public class DocInfo {
    @SerializedName("title")
    private String magTitle;

    @SerializedName("pagecount")
    private int pageCount;

    public String getMagTitle() {
        return magTitle;
    }

    public void setMagTitle(String magTitle) {
        this.magTitle = magTitle;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
