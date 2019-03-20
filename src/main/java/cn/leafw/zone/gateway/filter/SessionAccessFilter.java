package cn.leafw.zone.gateway.filter;

import cn.leafw.zone.common.dto.ResponseDto;
import cn.leafw.zone.gateway.rpc.IUserService;
import cn.leafw.zone.gateway.service.TokenService;
import cn.leafw.zone.gateway.utils.IOUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author CareyWYR
 * @description （用一句话描述这个类的作用）
 * @date 2018/7/4 10:56
 */
@Component
@Slf4j
public class SessionAccessFilter extends ZuulFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private IUserService iUserService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        System.out.println(request.getRequestURL());
        if(request.getRequestURL().toString().contains("/user/login") ){
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        if(request.getRequestURL().toString().contains("/user/login")){
            try {
                BufferedReader bufferedReader = request.getReader();
                String bodyStr = IOUtils.read(bufferedReader);
                log.info("body: {}", bodyStr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String userDto = request.getParameter("userDto");
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            ResponseDto responseDto = iUserService.checkUserLogin(userName, password);
            if(null == responseDto.getData()){
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(501);
                try {
                    context.getResponse().getWriter().write("userName password error");
                } catch (Exception e) {
                    log.error("filter exception: {}", e);
                }
            }
        }else {
            Cookie[] cookies = request.getCookies();
            boolean hasToken = false;
            boolean tokenValid = false;
            if(null != cookies){
                for (Cookie s : cookies) {
                    if("token".equals(s.getName())){
                        hasToken = true;
                        tokenValid = tokenService.validateToken(s.getValue());
                        log.info(s.getValue() + " --> " + tokenValid);
                        //非登出情况下每次操作刷新token失效时间，登出操作则手动将token失效
                        if(request.getRequestURL().toString().contains("/user/logout") && tokenValid){
                            tokenService.invalidToken(s.getValue());
                        }else{
                            tokenService.refreshTokenCache(s.getValue());
                        }
                    }
                }
            }
            if(!hasToken) {
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(401);
                try {
                    context.getResponse().getWriter().write("token is empty");
                } catch (Exception e) {
                    log.error("filter exception: {}", e);
                }
            }
            if(!tokenValid) {
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(402);
                try {
                    context.getResponse().getWriter().write("token is invalid");
                } catch (Exception e) {
                    log.error("filter exception: {}", e);
                }
            }
        }

        log.info("ok");
        return null;
    }
}
