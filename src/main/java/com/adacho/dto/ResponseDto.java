package com.adacho.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
	
	private Integer id;
	
	private String categoryName;
	
	private String placeName;
	
	private String roadAddressName;
	
	private String phone;
	
	private String placeUrl;
	
	private double x;
	
	private double y;
	
	private double rating;
	
	private String imgUrl;
}
