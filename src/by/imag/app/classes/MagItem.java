package by.imag.app.classes;


public class MagItem {
    private String magId; //140109103402-1931c53b51bfbd91c262c9e0f3308319
    private String magTitle; //Журнал \"Я\" 7(6) декабрь/январь 2013/14
    private String magUrl; //myself_december_january
    private String magImgUrl; //http://image.issuu.com/
    // 140109103402-1931c53b51bfbd91c262c9e0f3308319/jpg/page_1_thumb_large.jpg
    private int magPageCount; //112
    private long magTime; //140109103402

    public MagItem(String magId, String magTitle, String magUrl, String magImgUrl,
                   int magPageCount, long magTime) {
        this.magId = magId;
        this.magTitle = magTitle;
        this.magUrl = "http://issuu.com/vovic2000/docs/" + magUrl;
        this.magImgUrl = magImgUrl;
        this.magPageCount = magPageCount;
        this.magTime = magTime;
    }

    public MagItem(String magId, String magTitle, String magUrl, int magPageCount) {
        this.magId = magId;
        this.magTitle = magTitle;
        this.magUrl = "http://issuu.com/vovic2000/docs/" + magUrl;
        this.magImgUrl = "http://image.issuu.com/" + this.magId + "/jpg/page_1_thumb_large.jpg";
        this.magPageCount = magPageCount;
        this.magTime = Long.parseLong(this.magId.split("-")[0]);
    }

    public String getMagId() {
        return magId;
    }

    public void setMagId(String magId) {
        this.magId = magId;
    }

    public String getMagTitle() {
        return magTitle;
    }

    public void setMagTitle(String magTitle) {
        this.magTitle = magTitle;
    }

    public String getMagUrl() {
        return magUrl;
    }

    public void setMagUrl(String magUrl) {
        this.magUrl = magUrl;
    }

    public String getMagImgUrl() {
        return magImgUrl;
    }

    public void setMagImgUrl(String magImgUrl) {
        this.magImgUrl = magImgUrl;
    }

    public int getMagPageCount() {
        return magPageCount;
    }

    public void setMagPageCount(int magPageCount) {
        this.magPageCount = magPageCount;
    }

    public long getMagTime() {
        return magTime;
    }

    public void setMagTime(long magTime) {
        this.magTime = magTime;
    }
}
