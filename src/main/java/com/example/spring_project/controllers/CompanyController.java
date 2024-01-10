package com.example.spring_project.controllers;

import com.example.spring_project.beans.Coupon;
import com.example.spring_project.exception.ExceptionCoupons;
import com.example.spring_project.facade.ClientFacade;
import com.example.spring_project.facade.CompanyFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/company")
public class CompanyController {
    
    private HashMap<String, ClientFacade> tokensStore;
    private HttpServletRequest request;

    public CompanyController(HashMap<String, ClientFacade> tokensStore, HttpServletRequest request) {
        this.tokensStore = tokensStore;
        this.request = request;
    }

    @PostMapping("/addCoupon")
    public ResponseEntity<?>addCoupon(@RequestBody Coupon coupon) throws UnAuthorizedException{
        try {
            getFacade().addCoupon(coupon);
            return ResponseEntity.status(HttpStatus.CREATED).body(coupon);

        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCouponById(@PathVariable int couponId) throws UnAuthorizedException{
        try {
            getFacade().deleteCouponById(couponId);
            return ResponseEntity.ok().body("The coupon deleted!");
        }catch (ExceptionCoupons e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/getAllCompanyCoup")
    public ResponseEntity<?>getAllCompanyCoupons() throws UnAuthorizedException{
        return ResponseEntity.ok().body( getFacade().getCompanyCoupons());
    }

    @GetMapping("getCompanyDetails")
    public ResponseEntity<?>getCompanyDetails() throws UnAuthorizedException {
        try {
            return ResponseEntity.ok().body(getFacade().getCompanyDetails());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private CompanyFacade getFacade() throws UnAuthorizedException {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        CompanyFacade facade = (CompanyFacade) tokensStore.get(token);
        if(facade == null)
            throw new UnAuthorizedException("Trying to fool me? Login!");
        return facade;
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String> handleForbidden(UnAuthorizedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

//    @ExceptionHandler(ExceptionCoupons.class)
//    public ResponseEntity<String> handleCouponsException(ExceptionCoupons ex){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
}
