package controller;

import dto.ExpenseDto;
import dto.WishlistItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;
import java.io.IOException;

import static utils.Utils.validateToken;


@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class WishlistItemController {
    @Autowired
    private IService service;

    @PostMapping("/add-wishlistItem")
    public ResponseEntity<?> create(@RequestBody WishlistItemDto wishlistItemDto,
                                    @RequestHeader("Authorization") String bearerToken) {
        try {
            int userId = validateToken(bearerToken, service);
            wishlistItemDto.setUserId(userId);

            return new ResponseEntity<>(service.addWishlistItem(wishlistItemDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-wishlist-items")
    public ResponseEntity<?> getWishlistItems(@RequestHeader("Authorization") String bearerToken) {
        try {
            int userId = validateToken(bearerToken, service);
            return new ResponseEntity<>(service.getWishlistItems(userId), HttpStatus.OK);
        }
        catch (IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/purchase-wishlist-item/{wishlistItemId}")
    public ResponseEntity<?> purchaseWishlistItem(@RequestHeader("Authorization") String bearerToken, @PathVariable int wishlistItemId,
                                                  @RequestBody ExpenseDto expenseDto){
        try {
            int userId = validateToken(bearerToken, service);
            expenseDto.setUserId(userId);
            return new ResponseEntity<>(service.purchaseWishlistItem(wishlistItemId, expenseDto), HttpStatus.OK);
        }
        catch (ServiceException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-wishlist-item/{wishlistItemId}")
    public ResponseEntity<?> deleteWishlistItem(@RequestHeader("Authorization")String bearerToken,@PathVariable int wishlistItemId){
        try{
            int userId=validateToken(bearerToken,service);
            return new ResponseEntity<>(service.deleteWishlistItem(wishlistItemId, userId), HttpStatus.OK);

        }catch(ServiceException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/find-products")
    public ResponseEntity<?> findProducts(@RequestHeader("Authorization") String bearerToken,
                                          @RequestParam String keyword,
                                          @RequestParam String vendor) {
        try {
            validateToken(bearerToken, service);
            return new ResponseEntity<>(service.findProductsByKeywordAndVendor(keyword, vendor), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
