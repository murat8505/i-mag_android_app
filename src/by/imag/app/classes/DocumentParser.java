package by.imag.app.classes;


import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DocumentParser {
    private int pageNumber = 1;
    private Document document = null;

    public DocumentParser(int pageNumber) {
        this.pageNumber = pageNumber;
        this.document = parse();
    }

    public List<ArticlePreview> getArticlePreviewList() {
        List<ArticlePreview> articlePreviews = new ArrayList<ArticlePreview>();
        if (document != null) {
            Elements previews = document.select("div[class=preview]");
            logMsg("previews: " + previews.size());
            for (Element e: previews) {
                String articleTitle = "";
                String previewText = "";
                String articleURL = "";
                String imageURL = "";
                Elements hrefs = e.select("a[href]");
                if (hrefs.size() == 1) {
                    articleURL = hrefs.get(0).attr("href");
                }
                Elements imgs = hrefs.select("img");
                if (imgs.size() == 1) {
                    articleTitle = imgs.get(0).attr("title");
                    previewText = imgs.get(0).attr("alt");
                    imageURL = imgs.get(0).attr("src");
                }
                ArticlePreview articlePreview = new ArticlePreview(articleTitle, previewText,
                        articleURL, imageURL);
                articlePreviews.add(articlePreview);
            }
        } else  {
            logMsg("document: "+document);
        }
        return articlePreviews;
    }

    public int getCurrentPage() {
        int page = 0;
        if (document != null) {
            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
            Elements span = elementsNavigation.get(0).select("span[class=current]");
            String pageStr = span.get(0).text();
            page = Integer.parseInt(pageStr);
        }
        return page;
    }

    public int getLastPage() {
        int page = 0;
        if (document != null) {
            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
            Elements last = elementsNavigation.get(0).select("a[class=last]");
            String lastStr = last.get(0).attr("href");
//            logMsg("last: "+lastStr);
            String[] strings = lastStr.split("=");
            lastStr = strings[1];
            page = Integer.parseInt(lastStr);
        }
        return page;
    }

//    private void setDocument() {
//        this.document = parse();
//    }

    public Document parse() {
        logMsg("parsing");
        Document document = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Document> documentFuture = executorService.submit(new
                HtmlParserThread(pageNumber));
        try {
            document = documentFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        logMsg("finished");
        return document;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }
}
