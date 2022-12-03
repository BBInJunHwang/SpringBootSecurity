package com.ijhwang.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ijhwang.security1.model.SecurityUser;

// CRUD 함수를 JpaRepository 가 들고있음
// @Repository annotation 없어도 IOC 된다 => JpaRepository를 상속했기 때문에 빈으로 등록됨
// model, pk
public interface UserRepository extends JpaRepository<SecurityUser, Integer> {
	
	// findBy까지는 규칙이며, Username은 문법이다.
	// select * from SecurityUser(Entity 타입) where username =1? 
	// 1 = 파라미터 username
	// Jpa query method 검색해보면 어떻게 만들어지는지 규칙 알 수 있다.
	public SecurityUser findByUsername(String username);
	
	// ex)
	// select * from SecurityUser where email = 1?
	//public SecurityUser findByEmail(String email);

}
