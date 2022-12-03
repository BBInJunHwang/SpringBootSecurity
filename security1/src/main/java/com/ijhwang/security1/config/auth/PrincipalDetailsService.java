package com.ijhwang.security1.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ijhwang.security1.model.SecurityUser;
import com.ijhwang.security1.repository.UserRepository;


// SecurityConfig 설정에서 loginProcessingUrl("/login") 했으며
// /login 요청이 오면 자동으로 userDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 실핸된다.
@Service
public class PrincipalDetailsService implements UserDetailsService{

	private UserRepository userRepository;
	
	public PrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// 파라미터 username 은 loginForm.html 에 input name=username 값과 매칭되어야 한다.
	// 기본은 요청 파라미터명은 username 이며 만약 변경하고 싶으면 SecurityConfig 내 .usernameParameter("커스텀명"); 으로 등록해줘야 한다.
	// loginForm 에서 로그인 버튼 눌러서 /login 요청을 실행하면 
	// spring은 ioc 컨테이너에서 userDetailsService 로 등록된 타입을 찾고, loadUserByUsername을 호출한다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		SecurityUser securityUserEntity = userRepository.findByUsername(username);
		if(null != securityUserEntity) {
			
			// 이렇게 UserDetails 타입인 PrincipalDetails 를 리턴하게 되면
			// 시큐리티 세션 내부(Authentication 내부 (UserDetails 내부로 들어간다.))
			return new PrincipalDetails(securityUserEntity);
		}
		
		return null;
	}

}
