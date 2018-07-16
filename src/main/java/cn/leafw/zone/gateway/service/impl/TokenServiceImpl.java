package cn.leafw.zone.gateway.service.impl;

import cn.leafw.zone.gateway.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author CareyWYR
 * @description
 * @date 2018/7/16 10:34
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String generateToken() {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replaceAll("-","");
        log.info("token: {}", token);
        redisTemplate.opsForValue().set(token,token,600L, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public void refreshTokenCache(String token) {
        log.info("refresh token");
        redisTemplate.opsForValue().set(token,token,600L,TimeUnit.SECONDS);
    }

    @Override
    public void invalidToken(String token) {
        boolean deleteResult = redisTemplate.delete(token);
        if(deleteResult){
            log.info("token已删除");
        }else{
            log.error("token删除失败，失效或不存在！");
        }
    }

    @Override
    public boolean validateToken(String token) {
        String result = redisTemplate.opsForValue().get(token);
        if(null != result){
            return true;
        }
        return false;
    }
}
