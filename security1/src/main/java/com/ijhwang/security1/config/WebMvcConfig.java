package com.ijhwang.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration //IOC 등록 위해 설정
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		
		// mustache override 해서 재설정 가능
		// view 인코딩 UTF-8
		// 던지는 데이터는 html 파일이며,  UTF-8
		// classpath : 프로젝트 경로이며, /templates
		// .html 파일 만들 시 mustache 인식한다.
		// registry로 viewResolver 등록
		MustacheViewResolver resolver = new MustacheViewResolver();
		resolver.setCharset("UTF-8");
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		
		registry.viewResolver(resolver);
	}
}
