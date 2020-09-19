package blog.tsuchiya.todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import blog.tsuchiya.todo.interceptor.DirectoryIntercepter;

@Configuration
public class IntercepterConfig implements WebMvcConfigurer {
	
	@Autowired
	private DirectoryIntercepter di;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		// ルートとログアウトページ以外全てDirectoryIntercepterを実行
        registry.addInterceptor(di)
                .addPathPatterns("/**") 
                .excludePathPatterns("/",  "/logout");
    }
}
