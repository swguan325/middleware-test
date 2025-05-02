package swguan.middleware.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import swguan.middleware.support.header.HeaderTokenDTOInterceptor;

@Configuration
public class MiddlewareConfig implements WebMvcConfigurer {

	@Autowired
	private HeaderTokenDTOInterceptor headerTokenDTOInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(headerTokenDTOInterceptor).addPathPatterns("/v1/**");
	}
}