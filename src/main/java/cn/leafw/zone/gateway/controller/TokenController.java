package cn.leafw.zone.gateway.controller;

import cn.leafw.zone.common.dto.ResponseDto;
import cn.leafw.zone.gateway.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CareyWYR
 * @description
 * @date 2018/7/13 16:03
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getToken",method = RequestMethod.GET)
    public ResponseDto getToken(){
        String token = tokenService.generateToken();
        return ResponseDto.instance(token);
    }

    @RequestMapping(value = "/invalidToken",method = RequestMethod.GET)
    public ResponseDto invalidToken(@RequestParam(value = "token") String token){
        tokenService.invalidToken(token);
        return ResponseDto.instance(token);
    }
}
