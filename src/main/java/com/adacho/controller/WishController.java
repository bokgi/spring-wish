package com.adacho.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adacho.dto.DescriptionDto;
import com.adacho.dto.ResponseDto;
import com.adacho.dto.ShareDto;
import com.adacho.entity.WishList;
import com.adacho.service.WishListService;
import com.adacho.util.JwtUtil;
import com.adacho.util.ShareTokenProvider;


@RestController
@RequestMapping("/api/wish")
public class WishController {
	
	private final WishListService wishListService;
	private final JwtUtil jwtUtil;
	private final ShareTokenProvider shareTokenProvider;
	
	public WishController(WishListService wishListService, JwtUtil jwtUtil, ShareTokenProvider shareTokenProvider) {
		this.wishListService = wishListService;
		this.jwtUtil = jwtUtil;
		this.shareTokenProvider = shareTokenProvider;
	}
	
	@PostMapping("/add")
	public String addToList (@RequestHeader("Authorization") String authHeader, @RequestParam int id) {
		
        try {
    		String token = authHeader.replace("Bearer ", "");
    		String userId = jwtUtil.getUserIdFromToken(token);
    		System.out.println("WishController userid: " + userId);
    		
        	ResponseDto responseDto = new ResponseDto();
        	responseDto = wishListService.getRestaurantById(id);
        	System.out.println("responseDto: " + responseDto);
        	
        	wishListService.saveWish(userId, responseDto);
        	
            return "찜 목록에 추가했습니다!";
            
        } catch (Exception e) {
        	e.printStackTrace();
            return "찜 목록에 추가하지 못했습니다!";
            
        }
	}
	
	@GetMapping("/view")
	public List<WishList> getWishList(@RequestHeader("Authorization") String authHeader){

		try {
			
    		String token = authHeader.replace("Bearer ", "");
    		String userId = jwtUtil.getUserIdFromToken(token);
    		System.out.println("WishController userid: " + userId);
    		 		
			List<WishList> wishList = wishListService.findWishList(userId);	
			
			for(WishList wish : wishList) {
				System.out.println("wish: " + wish.getPlaceName() + wish.getAddress());
			}
			return wishList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
		
	}
	
	@PostMapping("/delete")
	public String deleteToList(@RequestHeader("Authorization") String authHeader, @RequestParam int id) {
		
		try {
			
    		String token = authHeader.replace("Bearer ", "");
    		String userId = jwtUtil.getUserIdFromToken(token);
    		System.out.println("WishController userid: " + userId);
    		
    		wishListService.deleteWish(userId, id);
    		
			return "찜 삭제 성공";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "찜 삭제 실패";
		}
	}
	
	@PostMapping("/description")
	public void updateDescription(@RequestHeader("Authorization") String authHeader, @RequestBody DescriptionDto descriptionDto) {
		
		try {
			
    		String token = authHeader.replace("Bearer ", "");
    		String userId = jwtUtil.getUserIdFromToken(token);
    		System.out.println("WishController userid: " + userId);
    		wishListService.updateWish(userId, descriptionDto);
    		
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@PostMapping("/share")
	public ShareDto viewForOthers(@RequestParam String token){
		
		ShareDto shareDto = new ShareDto();
		String userId = jwtUtil.getUserIdFromToken(token);
		String userName = wishListService.getUserName(userId);
		
		List<WishList> wishList = wishListService.findWishList(userId);	
		
		shareDto.setWishList(wishList);
		shareDto.setUserName(userName);
		
		return shareDto;
	}
	
	@PostMapping("/getToken")
	public String getToken(@RequestHeader("Authorization") String authHeader) {
		try {
			
    		String token = authHeader.replace("Bearer ", "");
    		String userId = jwtUtil.getUserIdFromToken(token);
    		String shareToken = shareTokenProvider.createShareToken(userId);
    		return shareToken;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
