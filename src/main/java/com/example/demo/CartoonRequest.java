package com.example.demo;

import java.util.List;

import lombok.Builder;
import lombok.Data;
	
	@Data
	@Builder
	public class CartoonRequest {
		private String cartoon;
		private List<String> characterNames;
	}
