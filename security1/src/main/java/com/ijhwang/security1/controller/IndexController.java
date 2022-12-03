package com.ijhwang.security1.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ijhwang.security1.model.SecurityUser;
import com.ijhwang.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public IndexController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@GetMapping({"","/"})
	public String index() {
		
		// 머스테치 -> 스프링에서 권장하는 view?
		// 기본폴더 /src/main/resources/
		// viewResolve 설정 시 : templates(prefix), mustache (suffix)
		// application.yml 설정
		// 하지만 안넣어도됨, 사용하겠다고 maven di 시 자동으로 잡아준다. 생략가능
		return "index"; // src/main/resources/templates/index.mustache 찾게됨 
		
		// 기본적으로 스프링 시큐리티 의존성 설정 시 모든 요청 막혀서 기본 로그인 페이지 나온다.
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	// 스프링 시큐리티 기본 로그인 주소가 /login 이여서 그냥 기본설정으로 이렇게 잡으면
	// 기본 시큐리티가 가로채서 여기 접근이 불가능하다.
	// SecurityConfig 파일 작성 후 작동안함 -> 커스텀 가능하다.
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(SecurityUser securityUser) {
		System.out.println(securityUser.toString());
		
		securityUser.setRole("ROLE_USER");
		String rawPassword = securityUser.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		securityUser.setPassword(encPassword);
		userRepository.save(securityUser); // 회원가입 되지만 비밀번호 1234 들어감 => security 로그인 시 불가능, 암호하 안되기 떄문
		
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") // 간단하게 걸고 싶은 부분에 권한을 걸어 줄 수 있다. SecurityConfig @EnableGlobalMethodSecurity 사용
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	// @Secured 문법은 하나만 가능?
	// @PreAuthorize 문법은 or 조건으로 여러개 가능하다?
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data() 메서드 실행 직전에 실행된다.
	//@PostAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 대부분 Pre를 쓰고 Post는 거의 안쓰며, @Secured가 등장 후 거의안씀? , 혹은 SecurityConfig에서 Global(전역으로)로 걸 수있다.
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
}
