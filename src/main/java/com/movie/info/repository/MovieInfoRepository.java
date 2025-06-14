package com.movie.info.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.movie.info.model.MovieInfo;

import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String>{

	Flux<MovieInfo> findByYear(Integer year);

	Flux<MovieInfo> findByName(String name);
}
