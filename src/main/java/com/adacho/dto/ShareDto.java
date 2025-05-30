package com.adacho.dto;

import java.util.List;

import com.adacho.entity.WishList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareDto {
	private List<WishList> wishList;
	private String userName;
}
