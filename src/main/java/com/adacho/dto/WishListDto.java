package com.adacho.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishListDto {
	
    private String place_name;
    
    private String place_url; 
    
    private String address; 
    
    private double rating;

    private String description;

    private String userId;
}
