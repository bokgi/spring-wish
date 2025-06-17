package com.adacho.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDeleteUpdateResponseDto {
	private String msg;
	
	public AddDeleteUpdateResponseDto(String msg) {
		this.msg = msg;
	}
}
