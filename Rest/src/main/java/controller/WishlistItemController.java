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

/**
 * The REST Controller responsible for exposing the endpoints related to managing the wishlist:
 * - add-wishlistItem
 * - get-wishlist-items
 * - purchase-wishlist-item
 * - delete-wishlist-item
 * - find-products
 */
@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class WishlistItemController {
    @Autowired
    private IService service;

    /**
     * Add a wishlist item
     * Method: POST
     * Requires Authorization header
     * Requires Content-Type: application/json header
     * @param wishlistItemDto the wishlist item to be saved
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return the wishlist item if successful, or an error message otherwise
     */
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

    /**
     * Get the items from wishlist
     * Method: GET
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return a list of wishlistItemViewModels
     */
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

    /**
     * Delete a wishlist item and adds an expense instead
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @param wishlistItemId the id of the wishlist item to be purchased
     * @param expenseDto the expense to be added
     * @return the added expense
     */
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

    /**
     * Delete a wishlist item
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @param wishlistItemId the id of the wishlist item to be deleted
     * @return the deleted item
     */
    @DeleteMapping("/delete-wishlist-item/{wishlistItemId}")
    public ResponseEntity<?> deleteWishlistItem(@RequestHeader("Authorization")String bearerToken,@PathVariable int wishlistItemId){
        try{
            int userId=validateToken(bearerToken,service);
            return new ResponseEntity<>(service.deleteWishlistItem(wishlistItemId, userId), HttpStatus.OK);

        }catch(ServiceException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Search for products by keyword and vendor
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @param keyword the keyword for filtering the products
     * @param vendor the vendor where to look for the products
     * @return a collection of WishlistItemViewModel, each representing a product
     */
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
