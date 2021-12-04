package service;

import domain.WishlistItem;

import java.io.IOException;
import java.util.List;

public interface ProductParser {
    List<WishlistItem> getProductsByKeyword(String keyword) throws IOException;
    List<WishlistItem> getProductsByKeyword(String keyword, int pageSize, int pageNumber) throws IOException;
    double computePrice(WishlistItem item) throws IOException;
}
