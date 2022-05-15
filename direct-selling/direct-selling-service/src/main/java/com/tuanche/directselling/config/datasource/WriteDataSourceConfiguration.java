package com.tuanche.directselling.config.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author gongbo
 * @description
 * @date 2020年3月4日17:22:13
 */
@Configuration
@MapperScan(basePackages = "com.tuanche.directselling.mapper.write.directselling", sqlSessionTemplateRef = "writeSqlSessionTemplate")
public class WriteDataSourceConfiguration {

    @Value("${tuanche.mysql.datasource.directselling-write.mapper-location}")
    private String mapperLocation;

    @Bean(name = "writeDataSource")
    @ConfigurationProperties(prefix = "tuanche.mysql.datasource.directselling-write")
    public DataSource writeDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "writeSqlSessionFactory")
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier("writeDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
        bean.setMapperLocations(resources);

        //分页插件
        return bean.getObject();
    }

    @Bean(name = "writeDataSourceTransactionManager")
    public DataSourceTransactionManager writeDataSourceTransactionManager(@Qualifier("writeDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "writeSqlSessionTemplate")
    public SqlSessionTemplate writeSqlSessionTemplate(@Qualifier("writeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
