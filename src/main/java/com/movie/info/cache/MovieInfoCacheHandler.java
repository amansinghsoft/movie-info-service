package com.movie.info.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.movie.info.model.MovieInfo;
import com.movie.info.service.MovieInfoService;

@Component
public class MovieInfoCacheHandler {

	@Autowired
	private MovieInfoService movieInfoService;
	
	@Cacheable(value = MovieInfoRedisCacheConfig.MOVIE_INFO_CONFIGURATION_CACHE,
			key = "#p0", cacheManager = MovieInfoRedisCacheConfig.MOVIE_INFO_CACHE_MANAGER)
	public MovieInfo getMovieInfoById( String id) {
		System.err.println("In Handler: ");
		return movieInfoService.getMovieInfoByIdOne(id);
	}
}
