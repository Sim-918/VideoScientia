package com.sim.video.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TmdbTvDto{
//    private Long id;
//    private String name;
//    private String posterPath;
//    private Double voteAverage;
//}
public record TmdbTvDto(
        Long id,
        String name,
        String posterPath,
        Double voteAverage
) {
}