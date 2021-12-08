package repository;

import domain.WishlistItem;

public interface IWishlistItemRepository extends IRepository<Integer, WishlistItem>{

    Iterable<WishlistItem> findByUser(int userId);
    Iterable<WishlistItem> getAffordableWishlistItems(int userId);
}
