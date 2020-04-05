package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

	@SpringBootApplication
	@RestController
	@RequestMapping("/demo")
	public class DemoApplication implements ApplicationRunner {
	
		public static void main(String[] args) {
			SpringApplication.run(DemoApplication.class, args);
		}
		
		Map<Integer, CartoonCharacter> characters;
	
		@Override
		public void run(ApplicationArguments args) throws Exception {
			String[] names = new String[] {"Ariel", "Prince Eric", "Sebastian", "Flounder"};
			characters = Arrays.asList( new CartoonCharacter[] {
					new CartoonCharacter(names[0].hashCode(), names[0], "Mermaid"), 
					new CartoonCharacter(names[1].hashCode(), names[1], "Human"), 
					new CartoonCharacter(names[2].hashCode(), names[2], "Crustacean"), 
					new CartoonCharacter(names[3].hashCode(), names[3], "Fish")} )
			.stream().collect(Collectors.toMap(CartoonCharacter::getId, Function.identity()));
			// TODO Auto-generated method stub
			CartoonRequest cr = CartoonRequest.builder()
			.cartoon("The Little Mermaid")
			.characterNames(Arrays.asList(names))
			.build();
			thisLocalClient
				.post()
				.uri("cartoonDetails")
				.body(Mono.just(cr), CartoonRequest.class)
				.retrieve()
				.bodyToFlux(CartoonCharacter.class)
				.subscribe(System.out::println);
		}
	
		@Bean
		WebClient localClient() {
			return WebClient.create("http://localhost:8080/demo/");
		}
		
		@Autowired
		WebClient thisLocalClient;
	
		@PostMapping("cartoonDetails")
		Flux<CartoonCharacter> getDetails(@RequestBody Mono<CartoonRequest> cartoonRequest) {
			Flux<StringWrapper> fn = cartoonRequest.flatMapIterable(cr->cr.getCharacterNames().stream().map(StringWrapper::new).collect(Collectors.toList()));
			Flux<Integer> ids = mapNamesToIds(fn);
			Flux<CartoonCharacter> details = mapIdsToDetails(ids);
			return details;
		}
		//  Service Layer Methods
		private Flux<Integer> mapNamesToIds(Flux<StringWrapper> names) {
			return thisLocalClient
				.post()
				.uri("findIds")
				.body(names, StringWrapper.class)
				.retrieve()
				.bodyToFlux(Integer.class);
		}
		private Flux<CartoonCharacter> mapIdsToDetails(Flux<Integer> ids) {
			return thisLocalClient
				.post()
				.uri("findDetails")
				.body(ids, Integer.class)
				.retrieve()
				.bodyToFlux(CartoonCharacter.class);
		}
		// Services
		@PostMapping("findIds")
		Flux<Integer> getIds(@RequestBody Flux<StringWrapper> names) {
			return names.map(name->name.getString().hashCode());
		}
		@PostMapping("findDetails")
		Flux<CartoonCharacter> getDetails(@RequestBody Flux<Integer> ids) {
			return ids.map(characters::get);
		}
	}
