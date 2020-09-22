package blog.tsuchiya.todo.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final int DEFAULT_START_PAGE = 0;
	private final int DEFAULT_PAGE_SIZE = 5;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		// ページ単位に表示する件数
		resolver.setFallbackPageable(PageRequest.of(DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE));
		argumentResolvers.add(resolver);
	}

}