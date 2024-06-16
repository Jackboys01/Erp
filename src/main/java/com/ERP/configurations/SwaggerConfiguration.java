package com.ERP.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info=@Info(title = "ERP APIs",
        description = "These apis are for ERP Project",
        summary = "Apis contains Summary Info",
//        termsOfService = "Terms & Conditions",
//        contact = @Contact(name="Developed by Simranjit Kaur, Daksh Malik, Raman Kumar", email = "simranjit.kaur@avisoft.io" // ,url="...")
//        ),
//        ,license = @License(name="nameOfLicense")
       version = "Api/V1"
      )
//        ,servers={@Server(description = "testEnv", url= "http://localhost:8080"),
//                @Server(description = "devEnv", url="http://localhost/8080")
//},
//        security = @SecurityRequirement(name = "erpSecurity")
)
//@SecurityScheme(name = "erpSecurity",in = SecuritySchemeIn.HEADER,type = SecuritySchemeType.HTTP,bearerFormat = "JWT",scheme = "bearer",description = "This is security for ERP application")
public class SwaggerConfiguration
{
}
