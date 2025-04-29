package com.sim.video.controller;

import com.sim.video.dto.TmdbMovieDto;
import com.sim.video.dto.TmdbTvDto;
import com.sim.video.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth= SecurityContextHolder.getContext().getAuthentication(); //로그인한 사용자의 인증정보 가져오기
        //Authentication 객체 안에 사용자 ID, 사용자 권한 목록, 로그인 되어 있는지 여부, 진짜 사용자 객체(좋아요, 즐겨찾기, 프로필 보기 전부 여기서 꺼낸 정보)

        List<TmdbMovieDto> movies = tmdbService.getPopularMovies(); //tmdb api가져오기
        model.addAttribute("popularMovies", movies);

        if (movies.isEmpty()) {
            model.addAttribute("errorMovieMessage", "TMDB에서 인기 영화를 불러올 수 없습니다.");
        }

        // 로그인하지 않은 상태인지 확인
        if(auth==null|| !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")){
            model.addAttribute("isLogin",false);
        }else{
            //로그인 성공한 사용자 이름 가져오기
            String username= auth.getName(); //로그인한 사용자 아이디 가져오기
            model.addAttribute("isLogin",true);
            model.addAttribute("username",username);
        }

        return "home";
    }

    @ResponseBody
    @GetMapping("/popularTV")
    public List<TmdbTvDto> getPopularTV() {
        List<TmdbTvDto> tvList = tmdbService.getPopularTV();
//        System.out.println("/popularTv 요청 들옴");
        // TV가 비어있으면 JS가 처리
        return tvList;
    }
}