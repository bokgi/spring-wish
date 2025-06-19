package com.adacho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.adacho.dto.DescriptionDto;
import com.adacho.dto.ResponseDto;
import com.adacho.entity.WishList;
import com.adacho.repository.WishListRepository;

@Service
public class WishListService {

	private WishListRepository wishListRepository;
	private final WebClient webClient = WebClient.create("http://nlp-app:8081");
	private final WebClient webClient2 = WebClient.create("http://login-app:8080");

	public WishListService(WishListRepository wishListRepository) {
		this.wishListRepository = wishListRepository;
	}

	public ResponseDto getRestaurantById(int id) {
		return webClient.get().uri(uriBuilder -> uriBuilder.path("/restaurant/search").queryParam("id", id).build())
				.retrieve().bodyToMono(ResponseDto.class).block(); // 결과 하나만 받음
	}

	public void saveWish(String userId, ResponseDto dto) {

		WishList wish = new WishList();

		wish.setUserId(userId);
		wish.setPlaceName(dto.getPlaceName());
		wish.setPlaceUrl(dto.getPlaceUrl());
		wish.setAddress(dto.getRoadAddressName());
		wish.setRating(dto.getRating());
		wish.setImgUrl(dto.getImgUrl());
		wish.setPhone(dto.getPhone());
		wish.setRestaurantId(dto.getRestaurantId());

		wishListRepository.save(wish);
	}

	public List<WishList> findWishList(String userId) {
		List<WishList> wishList = wishListRepository.findByUserId(userId);
		return wishList;
	}

	@Transactional
	public void deleteWish(String userId, int restaurantId) {

		wishListRepository.deleteByUserIdAndRestaurantId(userId, restaurantId);

	}

	public void updateWish(String userId, DescriptionDto descriptionDto) {

		Optional<WishList> optionalWish = wishListRepository.findByUserIdAndRestaurantId(userId,
				descriptionDto.getRestaurantId());
		if (optionalWish.isPresent()) {
			WishList wish = optionalWish.get();
			wish.setDescription(descriptionDto.getDescription());
			wishListRepository.save(wish);
		}


	}

	public String getUserName(String userId) {
		return webClient2.get().uri(uriBuilder -> uriBuilder.path("/get/name").queryParam("userId", userId).build())
				.retrieve().bodyToMono(String.class).block(); // 결과 하나만 받음
	}

}