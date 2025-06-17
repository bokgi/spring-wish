package com.adacho.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class addResponseDto {
	private String msg;
	
	public addResponseDto(String msg) {
		this.msg = msg;
	}
}
