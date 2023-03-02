package cn.erym.cloud.config;

import org.casbin.annotation.CasbinDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CasbinDataSourceConfiguration {
    @Bean
    @CasbinDataSource
    public DataSource casbinDataSource() {
        return DataSourceBuilder.create().url("jdbc:mysql://120.46.213.254:3306/casbin?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai").username("root").password("Dhj1314520").build();
    }
}
