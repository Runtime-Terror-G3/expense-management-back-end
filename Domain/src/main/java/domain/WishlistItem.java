package domain;

import dto.WishlistItemDto;
import viewmodel.ExpenseViewModel;
import viewmodel.WishlistItemViewModel;

import java.util.Objects;

public class WishlistItem implements Entity<Integer> {
    private int id;
    private String title;
    private double price;
    private String link;
    private String image;
    private WishlistItemVendor vendor;
    private User user;


    public WishlistItem() {
    }

    public WishlistItem(String title, double price, String link, String image, WishlistItemVendor vendor, User user) {
        this.title = title;
        this.price = price;
        this.link = link;
        this.image = image;
        this.vendor = vendor;
        this.user = user;
    }

    public WishlistItem(int id,String title, double price, String link, String image, WishlistItemVendor vendor) {
        this.title = title;
        this.price = price;
        this.link = link;
        this.image = image;
        this.vendor = vendor;
        this.id = id;
    }
    public WishlistItem(int id,String title, double price, String link, String image, WishlistItemVendor vendor,User user) {
        this.title = title;
        this.price = price;
        this.link = link;
        this.image = image;
        this.vendor = vendor;
        this.id = id;
        this.user=user;
    }



    /**
     * transforms an WishlistItemDto object to an WishlistItem object
     */
   public static WishlistItem fromWishlistItemDto(WishlistItemDto wishlistItemDto) {
        return new WishlistItem(
                wishlistItemDto.getTitle(),
                wishlistItemDto.getPrice(),
                wishlistItemDto.getLink(),
                wishlistItemDto.getImage(),
                wishlistItemDto.getVendor(),
                new User(wishlistItemDto.getUserId())
        );
    }

    public WishlistItemViewModel toWishlistItemViewModel(){
        return new WishlistItemViewModel(this.id,this.title,this.price,this.link,this.image,this.vendor);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItem item = (WishlistItem) o;
        return id == item.id && Double.compare(item.price, price) == 0 && vendor == item.vendor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, vendor);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "WishlistItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", vendor=" + vendor +
                '}';
    }


}
