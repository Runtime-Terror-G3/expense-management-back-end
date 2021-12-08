package service.productParser;

import domain.WishlistItem;
import service.exception.ServiceException;

import java.io.IOException;
import java.util.List;

public interface ProductParser {
    /**
     * Method for retrieving the products offered by a vendor that match a certain keyword
     * @param keyword, the text to search the products for
     * @return a {@code List} containing products that match {@param keyword}. An empty list is returned if any errors occur.
     * @throws IOException, if retrieving the information from the vendor's website fails
     */
    List<WishlistItem> getProductsByKeyword(String keyword) throws IOException;

    /**
     * Method for computing the price dynamically of a product, by accessing its dedicated page on the vendor's website
     * @param item, the item to compute the price for
     * @return the current price for {@param item}
     * @throws IOException, if retrieving the information from the vendor's website fails
     * @throws ServiceException if the url of the item is not in the correct format
     */
    double computePrice(WishlistItem item) throws IOException, ServiceException;
}
