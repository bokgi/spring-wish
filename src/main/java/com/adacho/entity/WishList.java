package com.adacho.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "wish_list")  // 실제 MySQL 테이블 이름
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;    
    
    private String placeUrl; 
    
    private String address; 
    
    private double rating;

    private String description;

    private String userId;
    
    private String imgUrl;
    
    private String phone;
    
    private Integer restaurantId;

    // 생성자
    public WishList() {}

}

