package id.eiris.newsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api")
                .allowedMethods("GET")
                .allowedOrigins("*")
                .maxAge(3600);
    }
    @Bean
    public WebFilter corsFilter(){
        return (ServerWebExchange ctx, WebFilterChain chain)->{
            ServerHttpRequest request=ctx.getRequest();
            if(CorsUtils.isCorsRequest(request)){
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin","*");
                headers.add("Access-Control-Allow-Methods","GET, PUT, POST, DELETE, OPTIONS");
                headers.add("Access-Control-Max-Age","3600");
                headers.add("Access-Control-Allow-Headers","x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
                if(request.getMethod()== HttpMethod.OPTIONS){
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }
}
