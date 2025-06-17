package com.adacho.controller;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.adacho.dto.AddDeleteUpdateResponseDto;
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
	public ResponseEntity<?> addToList(@RequestHeader("Authorization") String authHeader, @RequestParam int id) {

		try {
			String token = authHeader.replace("Bearer ", "");
			String userId = jwtUtil.getUserIdFromToken(token);
			System.out.println("WishController userid: " + userId);

			ResponseDto responseDto = new ResponseDto();
			responseDto = wishListService.getRestaurantById(id);
			System.out.println("responseDto: " + responseDto);

			wishListService.saveWish(userId, responseDto);

			return ResponseEntity.ok(new AddDeleteUpdateResponseDto("찜 목록에 추가했습니다!"));

		} catch (WebClientResponseException e) {
			System.out.println("***");
			e.printStackTrace();
			System.out.println("***");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");

		}
	}

	@GetMapping("/view")
	public List<WishList> getWishList(@RequestHeader("Authorization") String authHeader) {

		try {

			String token = authHeader.replace("Bearer ", "");
			String userId = jwtUtil.getUserIdFromToken(token);
			System.out.println("WishController userid: " + userId);

			List<WishList> wishList = wishListService.findWishList(userId);

			for (WishList wish : wishList) {
				System.out.println("wish: " + wish.getPlaceName() + wish.getAddress());
			}
			return wishList;
		} catch (DataAccessException e) {
			System.out.println("!!찜 목록을 불러오지 못했습니다!!");
			e.printStackTrace();
			return List.of();
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteToList(@RequestHeader("Authorization") String authHeader, @RequestParam int id) {

		try {

			String token = authHeader.replace("Bearer ", "");
			String userId = jwtUtil.getUserIdFromToken(token);
			System.out.println("WishController userid: " + userId);

			wishListService.deleteWish(userId, id);

			return ResponseEntity.ok(new AddDeleteUpdateResponseDto("찜 목록에서 삭제했습니다!"));
		} catch (CannotCreateTransactionException e) {
			System.out.println("!!찜 삭제 중 예외가 발생했어요!! ");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}

	@PostMapping("/description")
	public ResponseEntity<?> updateDescription(@RequestHeader("Authorization") String authHeader,
			@RequestBody DescriptionDto descriptionDto) {

		try {
			String token = authHeader.replace("Bearer ", "");
			String userId = jwtUtil.getUserIdFromToken(token);
			System.out.println("WishController userid: " + userId);
			wishListService.updateWish(userId, descriptionDto);
			return ResponseEntity.ok(new AddDeleteUpdateResponseDto("수정을 완료했습니다!"));
		} catch (DataAccessException e) {
			System.out.println("!!수정하는 중 예외가 발생했어요!! ");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}

	@GetMapping("/share")
	public ResponseEntity<?> viewForOthers(@RequestParam String token){
		
		try {
			ShareDto shareDto = new ShareDto();
			String userId = jwtUtil.getUserIdFromToken(token);
			String userName = wishListService.getUserName(userId);
			
			List<WishList> wishList = wishListService.findWishList(userId);	
			
			shareDto.setWishList(wishList);
			shareDto.setUserName(userName);
			
			return ResponseEntity.ok(shareDto);
			
		}catch (DataAccessException e){
			System.out.println("!!찜 목록을 불러오지 못했습니다!!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}

	@PostMapping("/getToken")
	public String getToken(@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String userId = jwtUtil.getUserIdFromToken(token);
			String shareToken = shareTokenProvider.createShareToken(userId);
			return shareToken;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
