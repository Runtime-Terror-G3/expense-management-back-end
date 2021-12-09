package service.productParser;

import domain.WishlistItem;
import domain.WishlistItemVendor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import service.productParser.parserutils.URLSafety;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraperServiceAltex implements ProductParser{

    private static final int PAGE_SIZE  = 24;
    private static final int PAGE_NUMBER  = 1;

    /**
     * Get the current price of the product
     * @param wishlistItem the product to get the price for
     * @return the current price of the given product
     * @throws IOException if something goes wrong with the Jsoup get or the url is malicious
     */
    @Override
    public double computePrice(WishlistItem wishlistItem) throws IOException {
        String url = wishlistItem.getLink();

        if(!URLSafety.isAltexValidURL(url)){
            throw new IOException("The url for the current item is malformed and might be malicious!");
        }

        Document doc = Jsoup.connect(url).get();

        String priceInt = doc.select(".Price-int.leading-none").get(0).text().replace(".", "");
        String priceDec = doc.select(".Price-int.leading-none").next().get(0).text().replace(',', '.');
        return Double.parseDouble(priceInt + priceDec);
    }

    /**
     * Get a list of products from Altex by a search keyword
     * ( this is a wrapper for setting pageSize=24 and pageNumber=1 )
     * @param keyword the key by which the search is made
     * @return a list of WishlistItems
     * @throws IOException if something goes wrong with the Jsoup get
     */
    @Override
    public List<WishlistItem> getProductsByKeyword(String keyword) throws IOException {
        return getProductsByKeyword(keyword, PAGE_SIZE, PAGE_NUMBER);
    }

    /**
     * Get a list of products from Altex by a search keyword
     * @param keyword the key by which the search is made
     * @param pageSize the number of elements to be displayed on a page
     * @param pageNumber the index of the page to get items from
     * @return a list of WishlistItems
     * @throws IOException if something goes wrong with the Jsoup get
     */
    private List<WishlistItem> getProductsByKeyword(String keyword, int pageSize, int pageNumber) throws IOException{
        List<WishlistItem> items = new ArrayList<>();
        keyword = URLSafety.sanitizeString(keyword);
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
}
