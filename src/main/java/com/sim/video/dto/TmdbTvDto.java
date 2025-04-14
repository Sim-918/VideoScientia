package com.sim.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbTvDto {
    private Long id;
    private String name;
    private String posterPath;
    private Double voteAverage;
}