package org.zerobase.scrapping;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zerobase.scrapping.model.Company;
import org.zerobase.scrapping.model.ScrapedResult;
import org.zerobase.scrapping.scrapper.YahooFinanceScrapper;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class ScrappingApplication {

    public static void main(String[] args) {
        	SpringApplication.run(ScrappingApplication.class, args);



    }

}
