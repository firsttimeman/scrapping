package org.zerobase.scrapping.scrapper;

import org.zerobase.scrapping.model.Company;
import org.zerobase.scrapping.model.ScrapedResult;

public interface Scrapper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
