package cn.erym.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableCaching
@MapperScan("cn.erym.cloud.mapper")
@ServletComponentScan(basePackages = "cn.erym.cloud")
@EnableTransactionManagement
@SpringBootApplication
@EnableFeignClients(basePackages = "cn.erym.cloud.feign")
public class Oauth2AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthApplication.class, args);
    }

}
