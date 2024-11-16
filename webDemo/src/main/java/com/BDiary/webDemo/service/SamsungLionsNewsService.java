package com.BDiary.webDemo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SamsungLionsNewsService {

    public List<NewsItem> crawlNews() {
        List<NewsItem> newsList = new ArrayList<>();

        try {
            // 웹사이트 연결
            Document doc = Jsoup.connect("https://www.samsunglions.com/fan/fan15.asp")
                    .userAgent("Mozilla/5.0")
                    .get();

            // 뉴스 목록 선택
            Elements newsElements = doc.select("#body > div > div.list");

            for (Element newsElement : newsElements) {
                // 각 뉴스 아이템 추출
                Elements newsItems = newsElement.select(".news-item");

                for (Element item : newsItems) {
                    NewsItem news = new NewsItem();

                    // 이미지 추출
                    Element imgElement = item.select("img").first();
                    if (imgElement != null) {
                        news.setImageUrl(imgElement.absUrl("src"));
                    }

                    // 제목 추출
                    Element titleElement = item.select(".title").first();
                    if (titleElement != null) {
                        news.setTitle(titleElement.text());
                    }

                    // 내용 추출
                    Element contentElement = item.select(".content").first();
                    if (contentElement != null) {
                        news.setContent(contentElement.text());
                    }

                    newsList.add(news);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsList;
    }

    // DTO 클래스
    public static class NewsItem {
        private String imageUrl;
        private String title;
        private String content;

        // Getters and setters
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}