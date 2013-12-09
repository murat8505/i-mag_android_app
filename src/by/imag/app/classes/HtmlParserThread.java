package by.imag.app.classes;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

public class HtmlParserThread implements Callable<Document>{
    private int pageNumber;
    private String pageURLString;

    public HtmlParserThread(int pageNumber) {
        this.pageNumber = pageNumber;
        this.pageURLString = Constants.PAGE + pageNumber;
    }

    @Override
    public Document call() throws Exception {
        Document htmlDocument = Jsoup.connect(pageURLString).get();
        return htmlDocument;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageURLString() {
        return pageURLString;
    }

    public void setPageURLString(String pageURLString) {
        this.pageURLString = pageURLString;
    }
}
