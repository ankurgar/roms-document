package com.ecs.sgws.poc.romsdocuments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisConfig {

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    //      redisStandaloneConfiguration.setDatabase(0);
    redisStandaloneConfiguration.setHostName("127.0.0.1");
    redisStandaloneConfiguration.setPort(6379);

    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

    jedisConnectionFactory.getPoolConfig().setMaxTotal(100);
    jedisConnectionFactory.getPoolConfig().setMaxIdle(10);

    return jedisConnectionFactory;
  }

}
