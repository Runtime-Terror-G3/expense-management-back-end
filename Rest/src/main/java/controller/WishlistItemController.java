package controller;

import domain.WishlistItemVendor;
import dto.WishlistItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;

import java.io.IOException;

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
    public ResponseEntity<?> getWishlistItems(@RequestParam int userId) {
        //TODO: get the userId from token
        return new ResponseEntity<>(service.getWishlistItems(userId), HttpStatus.OK);
    }

    @GetMapping("/get-affordable-wishlist-items")
    public ResponseEntity<?> getAffordableWishlistItems(@RequestParam int userId) {
        //TODO: get the userId from token

        return new ResponseEntity<>(service.getAffordableWishlistItems(userId), HttpStatus.OK);
    }

    @GetMapping("/find-products")
    public ResponseEntity<?> findProducts(@RequestParam String keyword, @RequestParam String vendor) {
        try {
            return new ResponseEntity<>(service.findProductsByKeywordAndVendor(keyword, vendor), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
