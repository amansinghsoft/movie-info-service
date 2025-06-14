package com.movie.info.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.movie.info.cache.MovieInfoCacheHandler;
import com.movie.info.exceptionhandler.GlobalErrorHandler;
import com.movie.info.model.MovieInfo;
import com.movie.info.service.MovieInfoService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
public class MovieInfoController {

	private final static Logger log = Logger.getLogger(MovieInfoController.class.getName());
	private MovieInfoService movieInfoService;
	private MovieInfoCacheHandler movieInfoCacheHandler;
	//Replay all added data to all subscriber (old and news)
	//Sinks.Many<MovieInfo> moviesInfoSink = Sinks.many().replay().all();
	
	// Only latest data found for new subscribers
	Sinks.Many<MovieInfo> moviesInfoSink = Sinks.many()
								.replay()
								.latest();
	
	public MovieInfoController(MovieInfoService movieInfoService,  MovieInfoCacheHandler movieInfoCacheHandler) {
		this.movieInfoService = movieInfoService;
		this.movieInfoCacheHandler = movieInfoCacheHandler;
	}
	
	@GetMapping("/ping")
	Mono<String> getMono(){
		
		return Mono.just("Pong")
				.log();
	}
	
	@PostMapping("/movieInfos")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid  MovieInfo movieInfo){
		//normal flow
		//return movieInfoService.addMovieInfo(movieInfo).log();
		
		//Sink FLow : pushblish continew to new moviews to subscribers
		return movieInfoService.addMovieInfo(movieInfo)
					.doOnNext(savedInfo -> moviesInfoSink.tryEmitNext(savedInfo));
		
	}
	
	@GetMapping(value = "/movieInfos/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<MovieInfo> getMovieInfoStream() {

	    return moviesInfoSink.asFlux().log();
	}
	
	@GetMapping("/movieInfos")
	public Flux<MovieInfo> getAllMovieInfos( @RequestParam(value = "year", required = false) Integer year, 
			@RequestParam(value = "name", required = false) String name){
		if(year !=null) {
			log.info("Search Param Year  : {} "+ year);
			return movieInfoService.getMovieInfoByYear(year);
		}
		if(name !=null) {
			log.info("Search Param Name  : {} "+ name);
			return movieInfoService.getMovieInfoByName(name);
		}
		return movieInfoService.getAllMovieInfos().log();
	}
	
	@GetMapping("/movieInfos/{id}")
	public Mono<MovieInfo> getMovieInfoById_approach2(@PathVariable("id") String id) {
		System.err.println("In Controller: ");
		/*
	    return movieInfoServiceCacheHandler.getMovieInfoById(id)
	            .map(movieInfo1 -> ResponseEntity.ok()
	                    .body(movieInfo1))
	            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
	            .log();
	    */
		return  movieInfoService.getMovieInfoById(id);
	}
	
	@PutMapping("/movieInfos/{id}")
	public Mono<MovieInfo> updateMovieInfo(@RequestBody MovieInfo movieInfo, @PathVariable String id){
		
		return movieInfoService.updateMovieInfo(movieInfo,  id).log();
	}
	
	@DeleteMapping("/movieInfos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteMovieInfo(@PathVariable String id){
		
		return movieInfoService.deleteMovieInfo(id).log();
	}
	
	@GetMapping("/movieInfos/cache/{id}")
	public Mono<ResponseEntity<MovieInfo>> getMovieInfoById_fromCache(@PathVariable("id") String id) {
		System.err.println("In Controller: ");
		/*
	    return movieInfoServiceCacheHandler.getMovieInfoById(id)
	            .map(movieInfo1 -> ResponseEntity.ok()
	                    .body(movieInfo1))
	            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
	            .log();
	    */
		// Using Redis Cache
				return Mono.just(ResponseEntity.ok().body( movieInfoCacheHandler.getMovieInfoById(id)));
			}
}
