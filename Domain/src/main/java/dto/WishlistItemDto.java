package dto;

import domain.User;
import domain.WishlistItem;
import domain.WishlistItemVendor;

import java.io.Serializable;
import java.util.Objects;

public class WishlistItemDto implements Serializable {
    private String title;
    private double price;
    private String link;
    private String image;
    private WishlistItemVendor vendor;
    private int userId;

    public WishlistItemDto(){ }
    public WishlistItemDto(String title, double price, String link, String image, WishlistItemVendor vendor, int userId) {
        this.title = title;
        this.price = price;
        this.link = link;
        this.image = image;
        this.vendor = vendor;
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItemDto item = (WishlistItemDto) o;
        return  Double.compare(item.price, price) == 0 && vendor == item.vendor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price,vendor);
    }

    @Override
    public String toString() {
        return "WishlistItemDto{" +
                "title="+title+
                "price=" + price +
                ", link=" + link +
                ", image=" + image +
                ", vendor=" + vendor +
                ", userId=" + userId +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
