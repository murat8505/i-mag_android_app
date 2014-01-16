package by.imag.app.json;


import com.google.gson.annotations.SerializedName;

public class DocOverResponse {
    @SerializedName("response")
    private DocResponse docResponse;

    public DocResponse getDocResponse() {
        return docResponse;
    }

    public void setDocResponse(DocResponse docResponse) {
        this.docResponse = docResponse;
    }

    public DocInfo getDocInfo() {
        return docResponse.getMagInfoItem();
    }
}
