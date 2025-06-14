package com.movie.info.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class MovieInfoRedisCacheConfig {

	public static final String MOVIE_INFO_CACHE_MANAGER = "MovieInfoCacheManager";
	public static final String MOVIE_INFO_CONFIGURATION_CACHE = "MovieInfoConfigurationCache";

	@Value("${spring.redis.cache-expire-time.cache-manager.movieinfo-configuration}")
	private String getMovieInfoCacheExpireTime;

	@Primary
	@Bean(name = MOVIE_INFO_CACHE_MANAGER)

	public RedisCacheManager confirmedCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {

		Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();

		cacheConfigurationMap.put(MOVIE_INFO_CONFIGURATION_CACHE,
				RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
						.entryTtl(Duration.ofMinutes(Integer.parseInt(getMovieInfoCacheExpireTime))));

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
				.withInitialCacheConfigurations(cacheConfigurationMap).build();
	}
}
