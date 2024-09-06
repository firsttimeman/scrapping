package org.zerobase.scrapping.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerobase.scrapping.model.Company;
import org.zerobase.scrapping.model.ScrapedResult;
import org.zerobase.scrapping.model.constants.CacheKey;
import org.zerobase.scrapping.persist.CompanyRepository;
import org.zerobase.scrapping.persist.DividendRepository;
import org.zerobase.scrapping.persist.entity.CompanyEntity;
import org.zerobase.scrapping.persist.entity.DividendEntity;
import org.zerobase.scrapping.scrapper.Scrapper;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@EnableCaching
public class ScrapperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scrapper yahooFinanceScrapper;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {

        log.info("scrapping is started");

        List<CompanyEntity> companies = companyRepository.findAll();

        for(CompanyEntity company : companies) {
//            log.info("scraping company : " + company.getName());
            ScrapedResult scrapedResult = yahooFinanceScrapper.scrap(
                    new Company(company.getTicker(), company.getName()));

            scrapedResult.getDividends().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists) {
                            dividendRepository.save(e);
                        }
                    });

            try {
                Thread.sleep(3000); // 3초
            } catch (InterruptedException e) {
               e.printStackTrace();
               Thread.currentThread().interrupt();
            }

        }



    }
}
