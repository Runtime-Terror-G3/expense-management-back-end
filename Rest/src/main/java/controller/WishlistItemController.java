package controller;

import dto.WishlistItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;

import static utils.Utils.validateToken;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class WishlistItemController {
    @Autowired
    private IService service;

    @PostMapping("/add-wishlistItem")
    public ResponseEntity<?> create(@RequestBody WishlistItemDto wishlistItemDto) {
        try {
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
        catch (Exception e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
