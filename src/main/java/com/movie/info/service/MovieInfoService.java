package com.movie.info.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.movie.info.model.MovieInfo;
import com.movie.info.repository.MovieInfoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

	private MovieInfoRepository repository;
	
	public MovieInfoService(MovieInfoRepository repository) {
		this.repository = repository;
	}

	public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
		return repository.save(movieInfo);
	}
	
	public Flux<MovieInfo> getMovieInfoByYear(Integer year) {
		// TODO Auto-generated method stub
		return repository.findByYear(year);
	}

	public Flux<MovieInfo> getMovieInfoByName(String name) {
		// TODO Auto-generated method stub
		return  repository.findByName(name);
	}
	
	public Flux<MovieInfo> getAllMovieInfos() {
		return repository.findAll();
	}
	
	public Mono<MovieInfo> getMovieInfoById(String id) {
		System.err.println("In Service: ");
		return repository.findById(id);
	}
	
	public Mono<MovieInfo> updateMovieInfo(MovieInfo updateMovieInfo, String id) {
		
		return repository.findById(id)
				.flatMap(movieInfo -> {
		 				movieInfo.setName(updateMovieInfo.getName());
		 				movieInfo.setRelease_date(updateMovieInfo.getRelease_date());
		 				movieInfo.setYear(updateMovieInfo.getYear());
		 				movieInfo.setCast(updateMovieInfo.getCast());
		 			
		 				return repository.save(movieInfo);
		 			}); 
	}
	
	public Mono<Void> deleteMovieInfo(String id) {
		// TODO Auto-generated method stub
		return repository.deleteById(id);
	}
	
	public MovieInfo getMovieInfoByIdOne(String id) {
		System.err.println("In Service: ");
		MovieInfo movieInfo = new MovieInfo();
		try {
			movieInfo = repository.findById(id).toFuture().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieInfo;
	}
}
