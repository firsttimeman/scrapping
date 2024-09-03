package org.zerobase.scrapping;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ScrappingApplication {

    public static void main(String[] args) {
        //	SpringApplication.run(ScrappingApplication.class, args);


        try {
            Connection connect = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history/?period1=99153000&period2=1725337212&frequency=1mo")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            Document document = connect.get();

            Element tableElement = document.selectFirst("table.table.yf-ewueuo");

			Element tbodyElement = tableElement.selectFirst("tbody");
			for (Element child : tbodyElement.children()) {
				String text = child.text();
				if(!text.endsWith("Dividend")) {
					continue;
				}
//				System.out.println(text);
				String[] split = text.split(" ");
				String month = split[0];
				int day = Integer.parseInt(split[1].replace(",", ""));
				int year = Integer.parseInt(split[2]);
				String dividend = split[3];

				System.out.println(year + "/" + month + "/" + " -> " + dividend);

			}


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
