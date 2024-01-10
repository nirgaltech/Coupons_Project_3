package com.example.spring_project.facade;

import com.example.spring_project.beans.Company;
import com.example.spring_project.beans.Coupon;
import com.example.spring_project.beans.Customer;
import com.example.spring_project.exception.ExceptionCoupons;
import com.example.spring_project.repository.CompanyRepository;
import com.example.spring_project.repository.CouponRepository;
import com.example.spring_project.repository.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Scope("prototype")
public class CompanyFacade extends ClientFacade{
    private int companyId;

    public CompanyFacade(CustomerRepository customerRepository, CouponRepository couponRepository, CompanyRepository companyRepository) {
        super(customerRepository, couponRepository, companyRepository);
    }
    /**
     * The method receives company's email and password and checks if the email and password are correct,
     * by method 'isCompanyExists' from the dao
     * @param email company's email
     * @param password company's password
     * @return true or false
     */
    @Override
    public boolean login(String email, String password) {
        if (companyRepo.existsByEmailAndPassword(email,password)){
            companyId = companyRepo.findByEmailAndPassword(email,password).getId();
            return true;
        }
        return false;
    }
    /**
     * The method receives a coupon object and checks if this company has the same title to another coupon,
     * if not, the method adds it to 'coupons' db by the method 'addCoupon' from the dao
     * @param coupon a coupon object
     * @throws ExceptionCoupons
     */
    public void addCoupon(Coupon coupon) throws ExceptionCoupons  {

        coupon.setCompany(getCompanyDetails());
        if (couponRepo.existsByTitleAndCompanyId(coupon.getTitle(),coupon.getCompany().getId())) {
            throw new ExceptionCoupons("This title  exists for your company");
        }
        if (coupon.getEndDate().before(coupon.getStartDate())){
            throw new ExceptionCoupons("Invalid end date");
        }
        if (coupon.getEndDate().before(Date.valueOf(LocalDate.now())))
            throw new ExceptionCoupons("Invalid end date");
        couponRepo.save(coupon);
    }

    /**
     *The method receives coupon's id and checks f the coupon is exists in the 'coupons' db
     * and then deletes the coupon purchase history by remove the coupon from the customer coupons list,
     * and finally deletes the coupon
     * @param couponId coupon's id
     * @throws ExceptionCoupons if the coupon is not exists
     */
    public void deleteCouponById(int couponId) throws ExceptionCoupons {
        if (couponRepo.existsById(couponId)) {
            List<Customer> customers = customerRepo.findAll();
            for (Customer cus : customers) {
                Set<Coupon> coupons = cus.getCoupons();
                coupons.removeIf(coup -> coup.getId() == couponId);
                cus.setCoupons(coupons);
                customerRepo.save(cus);
            }
            couponRepo.deleteById(couponId);
        }
        else
            throw new ExceptionCoupons("coupon  not exists");
    }
    /**
     *The method returns all the coupons from the db of the company that performed the login
     * @return a list of coupons object of this company
     */
    public List<Coupon> getCompanyCoupons(){
        return couponRepo.findByCompanyId(companyId);
    }

    /**
     *The method returns the details of the company that performed the login
     * @return the details of the company that performed the login
     * @throws ExceptionCoupons if the company is not exists
     */
    public Company getCompanyDetails() throws ExceptionCoupons {
       return companyRepo.findById(companyId).orElseThrow(()->new ExceptionCoupons("The company does not exist!"));
    }

}
