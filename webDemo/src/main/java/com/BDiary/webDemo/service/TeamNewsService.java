package com.BDiary.webDemo.service;

import com.BDiary.webDemo.entity.TeamNews;
import com.BDiary.webDemo.repository.TeamNewsRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@EnableScheduling
public class TeamNewsService {

    private final TeamNewsRepository teamNewsRepository;
    private static final Logger logger = LoggerFactory.getLogger(TeamNewsService.class);

    @Autowired
    public TeamNewsService(TeamNewsRepository teamNewsRepository) {
        this.teamNewsRepository = teamNewsRepository;
    }
    

    // 수동 크롤링
    // 호출하여 뉴스를 크롤링합니다.
    public void crawlAllTeamNews() {
        startNews();
    }

    public void startNews() {
        System.out.println("start news");

        DoosanBearsNews();
        SamsungLionsNews();
        /*
        GTwinsNews();
        HanwhaEaglesNews();
        KiwoomNews();
        ncDinosNews();
        LotteGiantNews();
        LandersNews();
        ktWizNews();
        SamsungLionsNews();
        KiaTigersNews();
         */
    }

    // 매일 자정에 시행
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleNewsCrawling() {
        System.out.println("뉴스 크롤링 스케줄러 시작: " + LocalDateTime.now());
        try {
            
            // 구단 뉴스/소식 크롤링
            DoosanBearsNews();
            SamsungLionsNews();
            
            System.out.println("뉴스 크롤링 완료: " + LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("스케줄된 크롤링 중 오류 발생: " + e.getMessage());
        }
    }

    // 한화이글스 뉴스 크롤링
    public void HanwhaEaglesNews(){

    }

    // 키움 뉴스 크롤링
    public void KiwoomNews(){

    }

    // nc 다이노스 뉴스 크롤링
    public void ncDinosNews(){

    }

    // 롯데 자이언트 뉴스 크롤링
    public void LotteGiantNews(){

    }

    // 랜더스 뉴스 크롤링
    public void LandersNews(){

    }

    // kt wiz 뉴스
    public void ktWizNews(){

    }

    // 두산 베어스 뉴스
    public void DoosanBearsNews() {
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");  // 브라우저 창 없이 실행
            driver = new ChromeDriver(options);

            // 두산 베어스 뉴스 페이지 접속
            driver.get("https://www.doosanbears.com/doorundoorun/news");
            System.out.println("두산 베어스 뉴스 페이지 로딩 시작");

            // JavaScript 실행 완료 대기
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.list-box")));

            // 뉴스 목록 가져오기
            List<WebElement> newsElements = driver.findElements(By.cssSelector("ul.list-box > li"));
            System.out.println("찾은 뉴스 개수: "+ newsElements.size());

            for (WebElement element : newsElements) {
                TeamNews news = new TeamNews();
                news.setTeamName("두산");
                news.setCategory("구단뉴스");

                try {
                    // 제목과 URL 추출
                    WebElement titleElement = element.findElement(By.cssSelector("a"));
                    if (titleElement != null) {
                        String title = titleElement.getText();

                        // 중복 체크
                        if (teamNewsRepository.existsByTeamNameAndTitle("두산", title)) {
                            System.out.println("중복된 뉴스 제목: " + title);
                            continue;  // 중복된 경우 다음 뉴스로 건너뛰기
                        }

                        // 중복이 아닌 경우에만 저장
                        news.setTitle(title);
                        news.setNewsUrl(titleElement.getAttribute("href"));
                        System.out.println("제목: " + title);
                    }

                    // 이미지 URL 추출 (있는 경우에만)
                    try {
                        WebElement imageElement = element.findElement(By.cssSelector("div.image_wrap"));
                        String style = imageElement.getAttribute("style");
                        if (style != null && style.contains("url(")) {
                            String imageUrl = style.substring(
                                    style.indexOf("url('") + 5,
                                    style.indexOf("')")
                            );
                            news.setImageUrl(imageUrl);
                            System.out.println("이미지 URL: " + imageUrl);
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("이미지 없는 뉴스입니다.");
                    }

                    // 작성 날짜
                    WebElement dateElement = element.findElement(By.cssSelector("p.txt"));
                    if (dateElement != null) {
                        String dateStr = dateElement.getText();
                        news.setCreatedAt(LocalDateTime.parse(dateStr + " 00:00:00",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        System.out.println("날짜: " + dateStr);
                    }

                    // DB에 저장
                    if (news.getTitle() != null && !news.getTitle().isEmpty()) {
                        TeamNews savedNews = teamNewsRepository.save(news);
                        System.out.println("저장된 뉴스 ID: " + savedNews.getId());
                    }
                } catch (Exception e) {
                    System.out.println("뉴스 항목 처리 중 오류: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("WebDriver 종료");
            }
        }
    }

    // 삼성 라이온즈 뉴스
    public void SamsungLionsNews() {
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            driver = new ChromeDriver(options);

            driver.get("https://www.samsunglions.com/fan/fan15.asp");
            System.out.println("삼성 라이온즈 뉴스 페이지 로딩 시작");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.list")));

            List<WebElement> newsElements = driver.findElements(By.cssSelector("div.list > ul > li.first"));
            System.out.println("찾은 뉴스 개수: "+ newsElements.size());

            for (WebElement element : newsElements) {
                try {
                    // 제목 추출
                    WebElement titleElement = element.findElement(By.cssSelector("div.txt > p"));
                    if (titleElement == null || titleElement.getText().isEmpty()) {
                        continue;
                    }

                    String title = titleElement.getText();

                    // 중복 체크
                    if (teamNewsRepository.existsByTeamNameAndTitle("삼성라이온즈", title)) {
                        System.out.println("중복된 뉴스 제목: "+ title);
                        continue;
                    }

                    TeamNews news = new TeamNews();
                    news.setTeamName("삼성라이온즈");
                    news.setCategory("구단 뉴스");
                    news.setTitle(title);


                    // URL 저장
                    WebElement linkElement = element.findElement(By.cssSelector("div.txt p a"));  // a 태그 직접 선택
                    if (linkElement != null) {
                        String newsUrl = linkElement.getAttribute("href");
                        if (newsUrl != null && !newsUrl.isEmpty()) {
                            if (!newsUrl.startsWith("http")) {
                                newsUrl = "https://www.samsunglions.com" + newsUrl;
                            }
                            news.setNewsUrl(newsUrl);
                            System.out.println("뉴스 URL: "+ newsUrl);
                        }
                    }

                    // 이미지 URL (있는 경우)
                    try {
                        WebElement imageElement = element.findElement(By.cssSelector("img"));
                        if (imageElement != null) {
                            String imageUrl = imageElement.getAttribute("src");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                if (!imageUrl.startsWith("http")) {
                                    imageUrl = "https://www.samsunglions.com" + imageUrl;
                                }
                                news.setImageUrl(imageUrl);
                                System.out.println("이미지 URL:" + imageUrl);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("이미지가 없는 뉴스입니다: " +  title);
                    }

                    // 작성 날짜 저장
                    WebElement dateElement = element.findElement(By.cssSelector("span.date"));
                    if (dateElement != null) {
                        String dateStr = dateElement.getText().trim();
                        // "작성일 : " 부분 제거하고 날짜 부분만 추출
                        if (dateStr.contains("작성일 : ")) {
                            dateStr = dateStr.replace("작성일 : ", "").split("\\|")[0].trim();
                            try {
                                // 날짜 형식 변환
                                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                news.setCreatedAt(date.atStartOfDay());  // 날짜를 자정(00:00:00)으로 설정
                                System.out.println("날짜: " + dateStr);
                            } catch (Exception e) {
                                System.out.println("날짜 파싱 오류: " + dateStr);
                            }
                        }
                    }

                    // DB에 저장
                    TeamNews savedNews = teamNewsRepository.save(news);
                    System.out.println("새로운 뉴스 저장 완료 - 제목: " + title + "ID: " + savedNews.getId());

                } catch (Exception e) {
                    System.out.println("뉴스 항목 처리 중 오류: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("WebDriver 종료");
            }
        }
    }

    // 기아 타이거즈
    public void KiaTigersNews(){

    }




    // LG 트윈스 뉴스 크롤링
    public void LGTwinsNews() {


    }

}

