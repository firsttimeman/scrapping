package org.zerobase.scrapping.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerobase.scrapping.model.Company;
import org.zerobase.scrapping.model.Dividend;
import org.zerobase.scrapping.model.ScrapedResult;
import org.zerobase.scrapping.persist.CompanyRepository;
import org.zerobase.scrapping.persist.DividendRepository;
import org.zerobase.scrapping.persist.entity.CompanyEntity;
import org.zerobase.scrapping.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {

    CompanyEntity companyEntity = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다"));

        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(companyEntity.getId());

        List<Dividend> dividends = dividendEntities.stream()
                        .map(e -> Dividend.builder()
                                .date(e.getDate())
                                .dividend(e.getDividend())
                                .build())
                                .collect(Collectors.toList());

     //   List<Dividend> dividends = new ArrayList<>();
//
//        for(var entity : dividendEntities) {
//            dividends.add(Dividend.builder()
//                            .date(entity.getDate())
//                            .dividend(entity.getDividend())
//                    .build());
//        }



        return new ScrapedResult(Company.builder()
                .ticker(companyEntity.getTicker())
                .name(companyEntity.getName())
                .build(), dividends);
    }
}
