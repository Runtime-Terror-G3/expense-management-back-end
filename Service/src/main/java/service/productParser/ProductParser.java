package service.productParser;

import domain.WishlistItem;

import java.io.IOException;
import java.util.List;

public interface ProductParser {
    /**
     * Method for retrieving the products offered by a vendor that match a certain keyword
     * @param keyword the text to search the products for
     * @return a {@code List} containing products that match the keyword
     * @throws IOException if retrieving the information from the vendor's website fails (e.g. the website does not respond)
     */
    List<WishlistItem> getProductsByKeyword(String keyword) throws IOException;

    /**
     * Method for computing the price dynamically of a product, by accessing its dedicated page on the vendor's website.
     * This method should only be called when a user requests to access their wishlist, in order to provide the current price for each product.
     * @param item the item to compute the price for
     * @return the current price for the item
     * @throws IOException if retrieving the information from the vendor's website fails (e.g. the website does not respond)
     */
    double computePrice(WishlistItem item) throws IOException;
}
