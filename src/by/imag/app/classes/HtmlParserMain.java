package by.imag.app.classes;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HtmlParserMain {
    private static final int startPageNumber = 1;
    private int page;
    private Document htmlDoc;

    public HtmlParserMain(int page) {
        this.page = page;
    }

    public HtmlParserMain() {
        htmlDoc = parse(startPageNumber);
    }

    public List<TagItem> getTags() {
        List<TagItem> tags = new ArrayList<TagItem>();
        if (htmlDoc != null ){
            Elements categories = htmlDoc.select("div[id=categories]");
            Elements titles = categories.select("a[title]");
            for (Element e: titles) {
                Elements span = e.select("span");
                Element spanElement = span.get(0);
                String tagNamePostCount = e.text();
                String tagName = tagNamePostCount.replaceAll("\\s\\d*$", "");
                String tagURL = e.attr("href");
                int postCount = Integer.parseInt(spanElement.text());
                TagItem tagItem = new TagItem(tagName, tagURL, postCount);
                tags.add(tagItem);
            }
        }
        return  tags;
    }

    private Document parse(int pageNumber) {
        Document document = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Document> documentFuture = executorService.submit(new
                HtmlParserThread(startPageNumber));
        try {
            document = documentFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        return document;
    }

    private int getCurrentPage() {
        int currentPage = -1;
        return currentPage;
    }

    private int getPagesCount() {
        int pagesCount = -10;
        return pagesCount;
    }
}
