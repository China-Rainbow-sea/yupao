package com.yupi.yupao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自定义 Swagger 接口文档的配置
 *
 * @author <a href="https://github.com/rainbowsea">Raibnowsea</a>
 */
@Configuration
//@EnableSwagger2WebMvc
@EnableSwagger2
@Profile({"dev", "test"})  // 表示该项目在什么样的开发环境下，对外开发接口文档
public class SwaggerConfig {

    @Bean(value = "defaultApi2")  //
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 这里一定要标注你控制器的位置
                .apis(RequestHandlerSelectors.basePackage("com.rainbowsea.yupao.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    /**
     * api 信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("鱼皮用户中心")
                .description("鱼皮用户中心接口文档")
                .termsOfServiceUrl("https://github.com/rainbowsea")
                .contact(new Contact("yupi", "https://github.com/rainbowsea", "xxx@qq.com"))
                .version("1.0")
                .build();
    }
}
