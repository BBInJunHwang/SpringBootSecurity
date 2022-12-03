package com.ijhwang.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // IOC 등록, 메모리에 떠야함
@EnableWebSecurity // 활성화 용도 => 스프링 시큐리티 필터가(SecurityConfig class) 스프링 필터체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화 IndexController /info 참고
public class SecurityConfig extends WebSecurityConfigurerAdapter{		  // @PreAuthorize, @PostAuthorize 어노테이션 활성화 IndexController /data 참고

	@Bean // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/user/**").authenticated()
		.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll() // 그 외는 모두 허용
		.and()
		.formLogin()   // /user, /manager, /admin 등 권한 없는 페이지 접근 시 403 떨어질때 /loginForm 으로 리디렉션 시킨다.
		.loginPage("/loginForm")
		//.usernameParameter("username2") // 기본 id 파라미터는 username이며 custom 하기 위해선 다음과 같이 설정해줘야 한다.
		.loginProcessingUrl("/login") // /login 요청이 호출되면 시큐리티가 낚아채서 대신 로그인 진행해준다 -> 컨트롤러에 /login 안 만들어도 된다. 시큐리티가 알아서 해준다
		.defaultSuccessUrl("/"); // 로그인 성공 시 이동할 기본 페이지, but 특정 페이지 ex) /user 요청했지만 로그인 미인증으로 로그인 페이지로 이동 후 
								 // 로그인 성공 시에는 /경로가 아닌 /user 페이지로 이동해준다. 
	}
	
}
