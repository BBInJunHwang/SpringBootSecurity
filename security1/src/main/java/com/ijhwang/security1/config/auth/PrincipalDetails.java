package com.ijhwang.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ijhwang.security1.model.SecurityUser;

// 시큐리티가 /login 주소 낚아채서 로그인 진행시킨다.
// 이떄 로그인 진행 완료시 security session 을 만들어준다.
// 같은 session 공간에서도 security session 공간이 따로 존재한다.
// Security ContextHolder key값에 세션정보 저장한다.

// Security Session에 들어갈 수 있는 object가 정해져 있으며
// object => Authentication 타입 객체 구조로 되어져 있다.
// Authentication 안에 User 정보가 있어야 하며, User object 타입은 UserDetails 타입 객체이다.

// 요약하면 Security Session 영역에 세션정보를 저장하며, 여기 들어갈 수 있는 객체는 Authentication 객체이며 => UserDetails 타입이다. 
// implements UserDetails 통해서 UserDetails 대신 PrincipalDetails를 넣을 수 있다.

// bean 등록 안한다 나중에 new로 메모리에 띄울거다.
public class PrincipalDetails implements UserDetails{

	private SecurityUser securityUser; // 콤포지션
	
	public PrincipalDetails(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}
	
	// 해당 유저 권한 리턴한다.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return securityUser.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return securityUser.getPassword();
	}

	@Override
	public String getUsername() {
		return securityUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		// 예시
		// 사이트에서 1년간 로그인 안 할시 휴면계정으로 변경 등 
		// SecurityUser model에 loginDate 있어야함.
		// securityUser.getLoginDate 가져온 후 현재시간과 비교해서 false 설정한다.
		
		return true;
	}

}
