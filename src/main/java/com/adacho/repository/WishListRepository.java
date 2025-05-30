package com.adacho.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adacho.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {
	
	 List<WishList> findByUserId(String userId);
	 
	 Optional<WishList> findByUserIdAndRestaurantId(String userId, int restaurantId);
	 
	 void deleteByUserIdAndRestaurantId(String userId, int id);
}
