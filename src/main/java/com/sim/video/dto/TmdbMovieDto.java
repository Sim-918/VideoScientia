package com.sim.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieDto {
    private Long id;
    private String title;
    private String posterPath;
    private Double voteAverage;
}
