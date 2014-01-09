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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentParser {
    private int pageNumber = 1;
    private String tagUrl;
    private Document document = null;

    public DocumentParser(int pageNumber) {
        this.pageNumber = pageNumber;
        this.document = parse();
    }

    public DocumentParser(String tagUrl) {
        this.tagUrl = tagUrl;
        this.document = parse(tagUrl);
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

    public List<TagItem> getTags() {
        List<TagItem> tags = new ArrayList<TagItem>();
        if (document != null ){
            Elements categories = document.select("div[id=categories]");
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
        return tags;
    }

    public List<ArchiveItem> getArchives() {
        List<ArchiveItem> archiveItems = new ArrayList<ArchiveItem>();
        if (document != null) {
            Elements archives = document.select("div[id=archives]");
//            logMsg("archives: "+archives);
            Elements archs = archives.select("a[href]");
//            logMsg("archs: "+archs);
            for (Element e: archs) {
                String archName = e.text();
//                logMsg("arch name: "+archName);
                String archUrl = e.attr("href");
//                logMsg("archUrl"+ archUrl);
                ArchiveItem archiveItem = new ArchiveItem(archName, archUrl);
                archiveItems.add(archiveItem);
            }
        }
        return archiveItems;
    }

    public int getCurrentPage() {
        int page = 0;
        if (document != null) {
            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
            Elements span = elementsNavigation.get(0).select("span[class=current]");
            String pageStr = span.get(0).text();
            page = Integer.parseInt(pageStr);
        }
        logMsg("current page: "+page);
        return page;
    }

    public int getLastPage() {
        int page = 0;
        if (document != null) {
            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
            Elements last = elementsNavigation.get(0).select("span[class=pages]");
            String lastStr = last.get(0).text();
            logMsg("last: "+lastStr);
//            String[] strings = lastStr.split("=");
//            lastStr = strings[1];
//            page = Integer.parseInt(lastStr);
//            Pattern lastIntPattern = Pattern.compile("\\s\\d*$");
//            Matcher matcher = lastIntPattern.matcher(lastStr);
//            if (matcher.find()) {
//                String lastPageStr = matcher.group(0);
//                page = Integer.parseInt(lastPageStr);
//                logMsg("last page: "+page);
//            }
            String[] pages = lastStr.split(" ");
            if (pages.length == 4) {
                page = Integer.parseInt(pages[3]);
            }

        }
        logMsg("last page: "+page);
        return page;
    }

    public int[] getPages() {
        int[] pages = new int[2];
        if (document != null) {
            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
            Elements elementsPages = elementsNavigation.get(0).select("span[class=pages]");
            if (elementsPages.size() == 1) {
                String response = elementsPages.get(0).text();
                String[] pagesStr = response.split(" ");
                pages[0] = Integer.parseInt(pagesStr[1]);
                pages[1] = Integer.parseInt(pagesStr[3]);
            }
        }
        return pages;
    }

//    private void setDocument() {
//        this.document = parse();
//    }

    private Document parse(String tagUrl) {
        logMsg("parsing");
        Document document = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Document> documentFuture = executorService.submit(new
                HtmlParserThread(tagUrl));
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

    private Document parse() {
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
