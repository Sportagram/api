package com.BDiary.webDemo.controller;

import com.BDiary.webDemo.entity.News;
import com.BDiary.webDemo.service.NewsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public String news(Model model) throws Exception{
        List<News> newsList = newsService.getNewsDatas();
        model.addAttribute("news", newsList);

        return "news";
    }

}
