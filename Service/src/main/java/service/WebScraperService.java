package service;

import domain.WishlistItem;
import domain.WishlistItemVendor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import service.exception.ServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraperService {

    /**
     * Get the details of the product by a given link
     * @param url the link to the product's page
     * @return a WishlistItem containing the needed details about the product
     * @throws ServiceException if something goes wrong while parsing the data (e.g. missing selector, unexpected data format)
     */
    public WishlistItem getWishlistItemByUrl(String url) throws ServiceException {
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

    /**
     * Get a list of products from Altex by a search keyword
     * ( this is a wrapper for setting pageSize=24 and pageNumber=1 )
     * @param keyword the key by which the search is made
     * @return a list of WishlistItems
     * @throws IOException if something goes wrong with the Jsoup get
     */
    List<WishlistItem> getProductsByKeyword(String keyword) throws IOException {
        return getProductsByKeyword(keyword, 24, 1);
    }

    /**
     * Get a list of products from Altex by a search keyword
     * @param keyword the key by which the search is made
     * @param pageSize the number of elements to be displayed on a page
     * @param pageNumber the index of the page to get items from
     * @return a list of WishlistItems
     * @throws IOException if something goes wrong with the Jsoup get
     */
    List<WishlistItem> getProductsByKeyword(String keyword, int pageSize, int pageNumber) throws IOException{
        List<WishlistItem> items = new ArrayList<>();
        String baseUrlAltex = "https://altex.ro/";
        String link = "https://fenrir.altex.ro/catalog/search/"+keyword+"?size="+pageSize+"&page="+pageNumber;

        Document doc = Jsoup.connect(link).ignoreContentType(true).get();

        JSONObject json = new JSONObject(doc.text());

        JSONArray products = (JSONArray) json.get("products");

        // for some keywords (e.g "laptop"), the fenrir API doesn't return the products, but redirects the user to another url
        if(products.isEmpty()){
            // get the url
            String url = ((JSONObject)json.getJSONArray("suggestedPages").get(0)).getString("url");
            // parse products from there
            return alternativeSuggestionAltex(url);
        }

        for(Object product:products){
            JSONObject obj = (JSONObject) product;

            String title = obj.getString("name");
            double price = obj.getDouble("price");
            String imageUrl = obj.getString("image");

            // the url of a product from Altex has the format https://altex.ro/[url_key]/cpd/[sku]
            String urlKey = obj.getString("url_key");
            String sku = obj.getString("sku");

            WishlistItem item = new WishlistItem();
            item.setPrice(price);
            item.setVendor(WishlistItemVendor.Altex);
            item.setLink(baseUrlAltex+urlKey+"/cpd/"+sku);
            item.setImage(imageUrl);
            item.setTitle(title);

            items.add(item);
        }

        return items;
    }

    /**
     * Fallback option for when the fenrir API doesn't return products, but suggests a link to be redirected to
     * @param url the suggested url
     * @return a list of WishlistItems
     * @throws IOException if something goes wrong with the Jsoup get
     */
    private List<WishlistItem> alternativeSuggestionAltex(String url) throws IOException {

        List<WishlistItem> items = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();

        for(Element element : doc.select(".Products").select("li")){
            WishlistItem item = new WishlistItem();

            String link = element.select("a").attr("href");
            String imageUrl = element.select("img").attr("src");
            String title = element.select(".Product-nameHeading.leading-20.text-sm.truncate-3-lines.min-h-60px").text();

            String priceInt = element.select(".Price-int").get(0).text().replace(".", "");
            String priceDec = element.select(".Price-dec").get(0).text().replace(',', '.');

            double price = Double.parseDouble(priceInt+priceDec);

            item.setTitle(title);
            item.setImage(imageUrl);
            item.setLink(link);
            item.setVendor(WishlistItemVendor.Altex);
            item.setPrice(price);

            items.add(item);
        }

        return items;
    }


    public static void main(String[] args) {
        String[] urls = {
                "https://altex.ro/laptop-gaming-asus-rog-strix-scar-15-g533qs-hq122-amd-ryzen-9-5900hx-pana-la-4-6ghz-15-6-qhd-32gb-ssd-2tb-nvidia-geforce-rtx-3080-16gb-free-dos-negru/cpd/LAPG533QSHQ122/",
                "https://altex.ro/microsistem-audio-panasonic-sc-pm250ecs-20w-bluetooth-usb-cd-radio-fm-argintiu/cpd/MICSCPM250ECS/",
                "https://altex.ro/vin-rosu-sec-domini-veneti-amarone-5l-cutie/cpd/VIN29373/",
                "https://altex.ro/telefon-apple-iphone-11-128gb-purple/cpd/SMTMWM52RMA"
        };
        try {
            for (String url : urls) {
                System.out.println(new WebScraperService().getWishlistItemByUrl(url));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(new WebScraperService().getProductsByKeyword("laptop"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
