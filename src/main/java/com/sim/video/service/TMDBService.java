package com.sim.video.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sim.video.dto.TmdbMovieDto;
import com.sim.video.dto.TmdbTvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service // 이 클래스를 Spring의 서비스 Bean으로 등록. 다른 클래스에서 @Autowired로 주입가능
@RequiredArgsConstructor //생성자를 자동을 생성하는 어노테이션
public class TMDBService {
    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    public List<TmdbMovieDto> getPopularMovies() {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=ko-KR&page=1";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                return results.stream().map(item -> {
                    Long id = ((Number) item.get("id")).longValue();
                    String title = (String) item.get("title");
                    String posterPath = (String) item.get("poster_path");
                    Double voteAverage = item.get("vote_average") instanceof Integer
                            ? ((Integer) item.get("vote_average")).doubleValue()
                            : (Double) item.get("vote_average");

                    return new TmdbMovieDto(id, title, posterPath, voteAverage);
                }).toList();
            } else {
                System.err.println("TMDB 응답 오류 (movies): results 없음");
            }
        } catch (Exception e) {
            System.err.println("TMDB 영화 API 호출 실패: " + e.getMessage());
        }

        return List.of(); // 실패 시 빈 리스트
    }

    public List<TmdbTvDto> getPopularTV() {
        String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + apiKey + "&language=ko-KR&page=1";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                return results.stream().map(item -> {
                    Long id = ((Number) item.get("id")).longValue();
                    String name = (String) item.get("name");
                    String posterPath = (String) item.get("poster_path");
                    Double voteAverage = item.get("vote_average") instanceof Integer
                            ? ((Integer) item.get("vote_average")).doubleValue()
                            : (Double) item.get("vote_average");

                    return new TmdbTvDto(id, name, posterPath, voteAverage);
                }).toList();
            } else {
                System.err.println("TMDB 응답 오류 (TV): results 없음");
            }
        } catch (Exception e) {
            System.err.println("TMDB TV API 호출 실패: " + e.getMessage());
        }

        return List.of(); // 실패 시 빈 리스트
    }
}