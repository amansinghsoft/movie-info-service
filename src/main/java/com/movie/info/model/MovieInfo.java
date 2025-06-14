package com.movie.info.model;


import java.io.Serializable;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Document
public class MovieInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String movieInfoId;
	
	@NotBlank(message = "movieInfo.name must be present")
	private String name;
	
	@NotNull
	@Positive(message = "movieInfo.year musst be a Positive value")
	private Integer year;

	private List<@NotBlank(message = "movieInfo.cast must be Present") String> cast;
	
	private LocalDate release_date;

	public String getMovieInfoId() {
		return movieInfoId;
	}

	public void setMovieInfoId(String movieInfoId) {
		this.movieInfoId = movieInfoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<String> getCast() {
		return cast;
	}

	public void setCast(List<String> cast) {
		this.cast = cast;
	}

	public LocalDate getRelease_date() {
		return release_date;
	}

	public void setRelease_date(LocalDate release_date) {
		this.release_date = release_date;
	}
	
	
}