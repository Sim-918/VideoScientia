package com.sim.video.controller;

import com.sim.video.dto.TmdbTvDto;
import com.sim.video.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TMDBService tmdbService;

    @GetMapping("/")
    public String home(Model model) {
        var movies = tmdbService.getPopularMovies();
        model.addAttribute("popularMovies", movies);

        if (movies.isEmpty()) {
            model.addAttribute("errorMovieMessage", "TMDB에서 인기 영화를 불러올 수 없습니다.");
        }

        return "home(real)";
    }

    @ResponseBody
    @GetMapping("/popularTV")
    public List<TmdbTvDto> getPopularTV() {
        var tvList = tmdbService.getPopularTV();
//        System.out.println("/popularTv 요청 들옴");
        // TV가 비어있으면 JS가 처리
        return tvList;
    }
}