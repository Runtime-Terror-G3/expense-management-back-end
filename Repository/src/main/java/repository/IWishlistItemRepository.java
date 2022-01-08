package repository;

import domain.WishlistItem;

public interface IWishlistItemRepository extends IRepository<Integer, WishlistItem>{

    /**
     * Find a user's wishlist items
     * @param userId the id of the user
     * @return a collection of WishlistItems
     */
    Iterable<WishlistItem> findByUser(int userId);

    /**
     * Find a user's affordable wishlist items
     * @param userId the id of the user
     * @return a collection of WishlistItems
     */
    Iterable<WishlistItem> getAffordableWishlistItems(int userId);
}
