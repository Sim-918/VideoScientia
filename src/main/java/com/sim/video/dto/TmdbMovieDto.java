package com.sim.video.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TmdbMovieDto {
//    private Long id;
//    private String title;
//    private String posterPath;
//    private Double voteAverage;
//}

public record TmdbMovieDto(
        Long id,
        String title,
        String posterPath,
        Double voteAverage
) {
}
