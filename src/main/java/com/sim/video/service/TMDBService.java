package com.sim.video.service;

import com.sim.video.dto.TmdbMovieDto;
import com.sim.video.dto.TmdbTvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service //이 클래스를 Spring의 서비스 Bean으로 등록, 다른 클래스에서 @Autowired로 주입가능
@RequiredArgsConstructor //생성자를 자동을 생성하는 어노테이션
public class TMDBService {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Cacheable(value = "popularMovies", unless = "#result.isEmpty()") //조건부 캐싱, 결과가 빈 리스트인 경우 캐싱 X
    public List<TmdbMovieDto> getPopularMovies() {
        System.out.println("TMDB API(Movie) 호출됨 (캐시 아님)"); //캐시테스트, 재 호출 시 안나와야함
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=ko-KR&page=1";
        return fetchMovieData(url);
    }

    @Cacheable(value = "popularTV", unless = "#result.isEmpty()")
    public List<TmdbTvDto> getPopularTV() {
        System.out.println("TMDB API(TV) 호출됨 (캐시 아님)"); //캐시테스트, 재 호출 시 안나와야함
        String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + apiKey + "&language=ko-KR&page=1";
        return fetchTvData(url);
    }

    private List<TmdbMovieDto> fetchMovieData(String url) {
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class); //tmdb get요청에 응답을 Map<String, Object> 타입으로 변환
            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                return results.stream(). //선언한 results(List<Map<String, Object>>)를 하나씩 꺼내서 쓸수있게 stream으로 객체 생성
                        map(item -> { //스트림의 각 요소를 변환
                    // 내부 변수 처리
                    Long id = ((Number) item.get("id")).longValue(); 
                    String title = (String) item.get("title");
                    String posterPath = (String) item.get("poster_path");

                    Object vote = item.get("vote_average");
                    Double voteAverage = (vote instanceof Number) ? ((Number) vote).doubleValue() : null;

                    return new TmdbMovieDto(id, title, posterPath, voteAverage); //추출한 데이터를 DTO객체를 하나 생성하여 반환
                }).collect(Collectors.toList()); //만들었던 DTO객체들을 하나의 List<Dto>로 모아서 반환(Collectors)
            }
        } catch (Exception e) {
            System.err.println("TMDB 영화 API 호출 실패: " + e.getMessage());
        }

        return Collections.emptyList(); //처리 도중 results가 비어있으면 빈 리스트로 반환
    }

    private List<TmdbTvDto> fetchTvData(String url) {
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                return results.stream().map(item -> {
                    Long id = ((Number) item.get("id")).longValue();
                    String name = (String) item.get("name");
                    String posterPath = (String) item.get("poster_path");

                    Object vote = item.get("vote_average");
                    Double voteAverage = (vote instanceof Number) ? ((Number) vote).doubleValue() : null;

                    return new TmdbTvDto(id, name, posterPath, voteAverage);
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("TMDB TV API 호출 실패: " + e.getMessage());
        }

        return Collections.emptyList();
    }
}