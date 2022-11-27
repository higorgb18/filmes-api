package com.api.movie.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MovieDto {
    @NotBlank
    private String movieName;
    @NotBlank
    private String movieGenre;
}
