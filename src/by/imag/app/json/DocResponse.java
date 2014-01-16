package by.imag.app.json;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DocResponse {
    @SerializedName("docs")
    ArrayList<DocInfo> magInfo;

    public DocInfo getMagInfoItem() {
        return magInfo.get(0);
    }

    public ArrayList<DocInfo> getMagInfo() {
        return magInfo;
    }

    public void setMagInfo(ArrayList<DocInfo> magInfo) {
        this.magInfo = magInfo;
    }
}
