package cn.leafw.zone.gateway.controller;

import cn.leafw.zone.common.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CareyWYR
 * @description
 * @date 2018/7/13 16:03
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @RequestMapping(value = "/getToken",method = RequestMethod.GET)
    public ResponseDto getToken(){
        return ResponseDto.instance("tokenStr");
    }
}
