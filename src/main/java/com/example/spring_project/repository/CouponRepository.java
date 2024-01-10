package com.example.spring_project.repository;

import com.example.spring_project.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    /**
     * the method get all the company coupons from DB
     * @param companyId the company id
     * @return list of coupons that the company have
     */
    List<Coupon> findByCompanyId(int companyId);

    /**
     * check if the coupon is exist by the company id and the title
     * @param title coupon title
     * @param companyId the company id
     * @return true or false
     */
    boolean existsByTitleAndCompanyId(String title,int companyId);


}
