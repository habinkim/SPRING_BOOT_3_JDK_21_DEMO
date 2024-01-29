package com.habin.demo.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.habin.demo.account.adapter.output.persistence.jwt.AccessToken;
import com.habin.demo.account.adapter.output.persistence.jwt.JwtToken;
import com.habin.demo.account.adapter.output.persistence.jwt.RefreshToken;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
@EnableTransactionManagement
public class RedisConfig {

    private static final String MAX_MEMORY_SETTING = "maxmemory 128M";
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisConfiguration redisConfiguration = LettuceConnectionFactory.createRedisConfiguration(redisURI());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private RedisURI redisURI() {
        return RedisURI.builder()
                .withHost(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .withDatabase(redisProperties.getDatabase())
                .withAuthentication(
                        redisProperties.getUsername() != null ? redisProperties.getUsername() : "",
                        redisProperties.getPassword() != null ? redisProperties.getPassword() : "")
                .build();
    }

    @Bean
    public RedisTemplate<String, JwtToken> jwtTokenRedisTemplate() throws JsonProcessingException {
        RedisTemplate<String, JwtToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JwtToken.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, AccessToken> redisTemplateForAccessToken() throws JsonProcessingException {
        RedisTemplate<String, AccessToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(AccessToken.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, RefreshToken> redisTemplateForRefreshToken() throws JsonProcessingException {
        RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() throws JsonProcessingException {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForObject() throws JsonProcessingException {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

}
