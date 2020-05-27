package com.tpvlog.epay.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterConfig {
    private String nodes;
    private Integer maxTotal;
    private Integer maxIdle;
    private Integer minIdle;

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool, RedisClusterConfiguration jedisConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisConfig, jedisPool);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JedisPoolConfig jedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxTotal);
        return jedisPoolConfig;
    }

    @Bean
    public RedisClusterConfiguration redisClusterConfigurationFactory() {
        RedisClusterConfiguration config = new RedisClusterConfiguration();

        String[] sub = nodes.split(",");
        List<RedisNode> nodeList = new ArrayList<>(sub.length);
        String[] tmp;
        for (String s : sub) {
            tmp = s.split(":");
            nodeList.add(new RedisNode(tmp[0], Integer.valueOf(tmp[1])));
        }

        config.setClusterNodes(nodeList);
        return config;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();

        return template;

    }


}
