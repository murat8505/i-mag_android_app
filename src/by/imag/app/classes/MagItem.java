package by.imag.app.classes;


public class MagItem {
    // todo: new object
    //http://image.issuu.com/140109103402-1931c53b51bfbd91c262c9e0f3308319/jpg/page_1_thumb_large.jpg
    //http://search.issuu.com/api/2_0/document?q=documentId:140109103402-1931c53b51bfbd91c262c9e0f3308319&responseParams=%2A
    //http://issuu.com/vovic2000/docs/

    private String magId;
    private String magImgUrl;
    private String magUrl;
    private long magTime;

    public MagItem(String magImgUrl, String magId, String magUrl) {
        this.magImgUrl = magImgUrl;
        this.magId = magId;
        this.magUrl = magUrl;
        this.magTime = Long.parseLong(magId.split("-")[0]);
    }

    public MagItem(String magId, String magUrl) {
        this.magImgUrl = "http://image.issuu.com/" + magId + "/jpg/page_1_thumb_large.jpg";
        this.magId = magId;
        this.magUrl = "http://issuu.com/vovic2000/docs/" + magUrl;
        this.magTime = Long.parseLong(magId.split("-")[0]);
    }

    public String getMagUrl() {
        return magUrl;
    }

    public void setMagUrl(String magUrl) {
        this.magUrl = magUrl;
    }

    public long getMagTime() {
        return magTime;
    }

    public void setMagTime(long magTime) {
        this.magTime = magTime;
    }

    public String getMagImgUrl() {
        return magImgUrl;
    }

    public void setMagImgUrl(String magImgUrl) {
        this.magImgUrl = magImgUrl;
    }

    public String getMagId() {
        return magId;
    }

    public void setMagId(String magId) {
        this.magId = magId;
    }
}
