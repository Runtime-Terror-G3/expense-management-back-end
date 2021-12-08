package service.productParser;

import domain.WishlistItem;
import domain.WishlistItemVendor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CelParser implements ProductParser {
    private static final String CEL_URL_SEARCH = "https://www.cel.ro/cauta/:keyword";

    @Override
    public List<WishlistItem> getProductsByKeyword(String keyword) {
        try {
            // CEL substitutes every space in the search with a plus sign in its URL
            keyword = keyword.replaceAll(" ", "+");
            Document document = Jsoup.connect(CEL_URL_SEARCH.replaceAll(":keyword", keyword)).get();

            return document
                    .select(".productlisting .product_data.productListing-tot .topArea")
                    .stream()
                    .map(element -> {
                        String title = element.select(".productListing-nume .productTitle a span").text();
                        double price = Double.parseDouble(element.select(".productListing-nume .price_part .pret_n .price").text());
                        String link = element.select(".productListing-nume .productTitle a").attr("href");
                        String imageUrl = element.select(".productListing-poza a img").attr("src");

                        return new WishlistItem(title, price, link, imageUrl, WishlistItemVendor.Cel, null);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public double computePrice(WishlistItem item) throws IOException {
        String price = Jsoup.connect(item.getLink()).get().select("#product-price").text();
        return Double.parseDouble(price);
    }
}
