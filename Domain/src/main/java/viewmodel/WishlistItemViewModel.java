package viewmodel;

import domain.WishlistItem;
import domain.WishlistItemVendor;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WishlistItemViewModel implements Serializable {
    private int id;
    private String title;
    private double price;
    private String link;
    private String image;
    private WishlistItemVendor vendor;
    private boolean isAffordable;

    public WishlistItemViewModel(){ }
    public WishlistItemViewModel(int id, String title, double price, String link, String image, WishlistItemVendor vendor) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.link = link;
        this.image = image;
        this.vendor = vendor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public WishlistItemVendor getVendor() {
        return vendor;
    }

    public void setVendor(WishlistItemVendor vendor) {
        this.vendor = vendor;
    }

    public boolean isAffordable() {
        return isAffordable;
    }

    public void setAffordable(boolean affordable) {
        isAffordable = affordable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItemViewModel that = (WishlistItemViewModel) o;
        return id == that.id && Double.compare(that.price, price) == 0 && vendor == that.vendor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, vendor);
    }

    /**
     * transforms an WishlistItem object to and WishlistItemViewModel object
     * @param wishlistItem the wishlist item to transform
     * @return a WishlistItemViewModel for the given item
     */
    public static WishlistItemViewModel fromWishlistItem(WishlistItem wishlistItem) {
        return new WishlistItemViewModel(
                wishlistItem.getId(),
                wishlistItem.getTitle(),
                wishlistItem.getPrice(),
                wishlistItem.getLink(),
                wishlistItem.getImage(),
                wishlistItem.getVendor()
        );
    }

    public static Iterable<WishlistItemViewModel> fromWishlistItemList(Iterable<WishlistItem> wishlistItems) {
        return StreamSupport.stream(wishlistItems.spliterator(), false)
                .map(WishlistItemViewModel::fromWishlistItem)
                .collect(Collectors.toList());
    }
}
