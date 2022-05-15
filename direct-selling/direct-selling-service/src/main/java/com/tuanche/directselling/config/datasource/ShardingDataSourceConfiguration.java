package com.tuanche.directselling.config.datasource;

import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/4/8 9:35
 **/
@Configuration
@MapperScan(basePackages = "com.tuanche.directselling.mapper.sharding", sqlSessionTemplateRef = "shardingSqlSessionTemplate")
public class ShardingDataSourceConfiguration {

    @Bean(name = "shardingDataSource")
    public DataSource getDataSource(@Qualifier("dataSourceMap") Map<String, DataSource> dataSourceMap) throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("fission_id", "fission_user_task_record_$->{fission_id % 5}"));
        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
    }

    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("fission_user_task_record", "master.fission_user_task_record_$->{0..4}");
        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        keyGeneratorConfiguration.getProperties().setProperty("worker.id", getWorkId());
        result.setKeyGeneratorConfig(keyGeneratorConfiguration);
        return result;
    }

    List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("master", "master", Arrays.asList("slave"));
        return Lists.newArrayList(masterSlaveRuleConfig);
    }

    @Bean(name = "dataSourceMap")
    Map<String, DataSource> dataSourceMap(@Qualifier("readDataSource") DataSource readDataSource, @Qualifier("writeDataSource") DataSource writeDataSource) {
        final Map<String, DataSource> result = new HashMap<>(8);
        result.put("master", writeDataSource);
        result.put("slave", readDataSource);
        return result;
    }


    @Bean(name = "shardingSessionFactory")
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier("shardingDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:conf/mybatis_config.xml"));
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/sharding/*.xml");
        bean.setMapperLocations(resources);
        return bean.getObject();
    }


    @Bean(name = "shardingTransactionManager")
    @Primary
    public DataSourceTransactionManager readDataSourceTransactionManager(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shardingSqlSessionTemplate")
    public SqlSessionTemplate writeSqlSessionTemplate(@Qualifier("shardingSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    private String getWorkId() {
        int[] ints = org.apache.commons.lang3.StringUtils.toCodePoints(getServerIp());
        int sums = 0;
        int[] var3 = ints;
        int var4 = ints.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            int b = var3[var5];
            sums += b;
        }

        long workId = (long) (sums % 32);
        return String.valueOf(workId);
    }

    private String getServerIp() {
        // 本地IP，如果没有配置外网IP则返回它
        String localip = null;
        // 外网IP
        String netip = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            // 是否找到外网IP
            boolean finded = false;
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress) address.nextElement();

                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        // 外网IP
                        netip = ip.getHostAddress();
                        finded = true;
                        break;

                    } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        // 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
}
