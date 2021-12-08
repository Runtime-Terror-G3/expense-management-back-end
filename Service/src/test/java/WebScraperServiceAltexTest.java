import domain.WishlistItem;
import org.junit.jupiter.api.Test;
import service.exception.ServiceException;
import service.productParser.WebScraperServiceAltex;

import java.io.IOException;
import java.util.List;

class WebScraperServiceAltexTest {
    String[] urls = {
            "https://altex.ro/laptop-gaming-asus-rog-strix-scar-15-g533qs-hq122-amd-ryzen-9-5900hx-pana-la-4-6ghz-15-6-qhd-32gb-ssd-2tb-nvidia-geforce-rtx-3080-16gb-free-dos-negru/cpd/LAPG533QSHQ122/",
            "https://altex.ro/microsistem-audio-panasonic-sc-pm250ecs-20w-bluetooth-usb-cd-radio-fm-argintiu/cpd/MICSCPM250ECS/",
            "https://altex.ro/vin-rosu-sec-domini-veneti-amarone-5l-cutie/cpd/VIN29373/",
            "https://altex.ro/telefon-apple-iphone-11-128gb-purple/cpd/SMTMWM52RMA"
    };

    WebScraperServiceAltex webScraper = new WebScraperServiceAltex();

    @Test
    void testgetWishlistItemByUrl(){

        System.out.println("testgetWishlistItemByUrl");
        try {
            for (String url : urls) {
                WishlistItem item = webScraper.getWishlistItemByUrl(url);
                System.out.println(item);
                assert(item.getPrice()>0);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testGetProductsByKeyword(){
        System.out.println("testGetProductsByKeyword");
        try {
            List<WishlistItem> items = webScraper.getProductsByKeyword("laptop");
            System.out.println(items);
            assert(items.size()>0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testComputePrice(){
        System.out.println("testComputePrice");
        WishlistItem item = new WishlistItem();
        item.setLink(urls[3]);
        Double price = 0.0;
        try {
            price = webScraper.computePrice(item);
            System.out.println(price);
            assert(price>0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
