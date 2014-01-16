package by.imag.app.json;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response {
    private int numFound;
    private int start;
    @SerializedName("docs")
    ArrayList<Doc> docs;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public ArrayList<Doc> getDocs() {
        return docs;
    }

    public void setDocs(ArrayList<Doc> docs) {
        this.docs = docs;
    }
}
