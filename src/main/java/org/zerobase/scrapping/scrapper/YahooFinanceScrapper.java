package org.zerobase.scrapping.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.zerobase.scrapping.model.Company;
import org.zerobase.scrapping.model.Dividend;
import org.zerobase.scrapping.model.ScrapedResult;
import org.zerobase.scrapping.model.constants.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScrapper implements Scrapper {

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?period1=%d&period2=%d&frequency=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400; // 60 * 60 * 24

    @Override
    public ScrapedResult scrap(Company company) {

        var scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;

           String URL = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            Connection connect = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            Document document = connect.get();

            Element parsingDivs = document.selectFirst("table.table.yf-ewueuo");

            Element tableElement = parsingDivs.selectFirst("tbody");
            List<Dividend> dividends = new ArrayList<>();
            for (Element child : tableElement.children()) {
                String text = child.text();
                if(!text.endsWith("Dividend")) {
                    continue;
                }
                String[] split = text.split(" ");
                int month = Month.strToNumber(split[0]);
                int day = Integer.parseInt(split[1].replace(",", ""));
                int year = Integer.parseInt(split[2]);
                String dividend = split[3];

                if(month < 0) {
                    throw new RuntimeException("Unexpected Month enum value ->" + split[0]);
                }

                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());

//                System.out.println(year + "/" + month + "/" + " -> " + dividend);

            }
        scrapedResult.setDividends(dividends);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        return scrapedResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();

           // Element titleElement = document.getElementsByTag("h1").get(0);
            Element titleEle = document.getElementsByTag("h1").get(1);
            String title = titleEle.text().split("\\(")[0].trim();

            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
