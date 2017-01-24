package org.seckill.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author Ryan-hou
 * @description:
 * @className: SwaggerConfig
 * @date January 24,2017
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    @Bean
    public Docket seckillApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                //.pathMapping("/")
                .select()
                //Ignores controllers annotated with @CustomIgnore
                //.apis(not(withClassAnnotation(CustomIgnore.class)) //Selection by RequestHandler
                .apis(RequestHandlerSelectors.any())
                //.paths( or(regex("/seckill/.*")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("秒杀系统后台API")
                .description("Seckill API v1.0")
                .contact(new Contact("Ryan", "", "676476208@qq.com"))
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}
