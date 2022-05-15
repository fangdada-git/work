package com.tuanche.directselling.config.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author liangguangzhi
 * @description
 * @date 2018/12/10 11:37 AM
 */
@Configuration
@MapperScan(basePackages = "com.tuanche.directselling.mapper.read.directselling", sqlSessionTemplateRef = "readSqlSessionTemplate")
public class ReadDataSourceConfiguration {

    @Value("${tuanche.mysql.datasource.login-user}")
    private String loginUser;

    @Value("${tuanche.mysql.datasource.login-password}")
    private String loginPassword;

    @Value("${tuanche.mysql.datasource.directselling-read.mapper-location}")
    private String mapperLocation;

    @Bean(name = "readDataSource")
    @Primary
    @ConfigurationProperties(prefix = "tuanche.mysql.datasource.directselling-read")
    public DataSource readDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 设置drui访问限制
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", loginUser);
        reg.addInitParameter("loginPassword", loginPassword);
        return reg;
    }

    @Bean(name = "readSqlSessionFactory")
    @Primary
    public SqlSessionFactory readSqlSessionFactory(@Qualifier("readDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:conf/mybatis_config.xml"));
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
        bean.setMapperLocations(resources);

        //分页插件
        return bean.getObject();
    }

    @Bean(name = "readDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager readDataSourceTransactionManager(@Qualifier("readDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "readSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate readSqlSessionTemplate(@Qualifier("readSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
