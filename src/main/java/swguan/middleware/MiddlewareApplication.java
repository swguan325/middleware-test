
package swguan.middleware;

import org.springframework.boot.actuate.autoconfigure.metrics.jersey.JerseyServerMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import swguan.middleware.support.ApplicationSupport;

@SpringBootApplication(exclude = JerseyServerMetricsAutoConfiguration.class)
@ComponentScan(value = {"swguan"})
public class MiddlewareApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ApplicationSupport.configureProfile();
        return application.sources(MiddlewareApplication.class);
    }

    public static void main(String[] args) {
        ApplicationSupport.run(MiddlewareApplication.class, args);
    }

}
