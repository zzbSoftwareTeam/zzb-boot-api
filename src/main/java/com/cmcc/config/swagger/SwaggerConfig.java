package com.cmcc.config.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.Parameter;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	/**
	 * 可以定义多个组，比如本类中定义把test和demo区分开了 （访问页面就可以看到效果了）
	 * http://localhost:8080/swagger-ui.html
	 */
	@Bean
	public Docket createRestApi() {
		
		ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();  
    	ticketPar.name("Authorization").description("用户鉴权token")
    	.modelRef(new ModelRef("string")).parameterType("header") 
    	.required(false).build(); //header中的ticket参数非必填，传空也可以
    	pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("test")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.pathMapping("/")// base，最终调用接口后会和paths拼接在一起
				.select()
				//.paths(PathSelectors.regex("/api/.*"))
				.apis(RequestHandlerSelectors.basePackage("com.cmcc"))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(pars)
				.apiInfo(myApiInfo());
		return docket;
	}

	private ApiInfo myApiInfo() {
		return new ApiInfoBuilder()
				.title("人员部门信息接口")
				.description("人员部门相关接口，用于测试")
                .version("1.0")
				.build();
	}

}
