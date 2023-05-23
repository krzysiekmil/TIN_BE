package pjwstk.s20124.tin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {
    public final static String FILE_DIR = "files";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        var locationPath = new StringJoiner("/").add(System.getProperty("user.home")).add(FILE_DIR).toString();
        log.info("Configuring static resource server for path: {}" , locationPath);
        registry
            .addResourceHandler("/files/**")
            .addResourceLocations("file:" + locationPath + "/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }
}
