package cn.leafw.zone.gateway.service;

/**
 * @author CareyWYR
 * @description
 * @date 2018/7/16 10:32
 */
public interface TokenService {

    /**
     * 生成token
     * @return
     */
    String generateToken();

    /**
     * 刷新token失效时间
     * @param token
     */
    void refreshTokenCache(String token);

    /**
     * 失效token
     * @param token
     */
    void invalidToken(String token);

    /**
     * 校验token有效性
     * @param token
     * @return
     */
    boolean validateToken(String token);
}
