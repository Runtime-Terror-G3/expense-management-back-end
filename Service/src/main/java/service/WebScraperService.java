package service;

import domain.WishlistItem;
import domain.WishlistItemVendor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import service.exception.ServiceException;

public class WebScraperService {

    /**
     * Get the details of the product by a given link
     * @param url the link to the product's page
     * @return a WishlistItem containing the needed details about the product
     * @throws ServiceException if something goes wrong while parsing the data (e.g. missing selector, unexpected data format)
     */
    public WishlistItem getWishlistItemAltex(String url) throws ServiceException {
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setLink(url);
        wishlistItem.setVendor(WishlistItemVendor.Altex);
        wishlistItem.setTitle("");
        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.select("div.mb-5 > div.mb-1 > h1 > div").get(0).text();

            String imageUrl = doc.select("div.slick-slide.slick-active.slick-current").get(0).select("img").attr("src");

            String priceInt = doc.select(".Price-int.leading-none").get(0).text().replace(".", "");
            String priceDec = doc.select(".Price-int.leading-none").next().get(0).text().replace(',', '.');
            double price = Double.parseDouble(priceInt + priceDec);

            wishlistItem.setTitle(title);
            wishlistItem.setPrice(price);
            wishlistItem.setImage(imageUrl);
        }catch (Exception exception){
            throw new ServiceException("Something went wrong while trying to get the product details from Altex!");
        }

        return wishlistItem;
    }

    public static void main(String[] args) {
        try {

            String[] urls = {
                    "https://altex.ro/laptop-gaming-asus-rog-strix-scar-15-g533qs-hq122-amd-ryzen-9-5900hx-pana-la-4-6ghz-15-6-qhd-32gb-ssd-2tb-nvidia-geforce-rtx-3080-16gb-free-dos-negru/cpd/LAPG533QSHQ122/",
                    "https://altex.ro/microsistem-audio-panasonic-sc-pm250ecs-20w-bluetooth-usb-cd-radio-fm-argintiu/cpd/MICSCPM250ECS/",
                    "https://altex.ro/vin-rosu-sec-domini-veneti-amarone-5l-cutie/cpd/VIN29373/"
            };
            for (String url : urls) {
                System.out.println(new WebScraperService().getWishlistItemAltex(url));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
