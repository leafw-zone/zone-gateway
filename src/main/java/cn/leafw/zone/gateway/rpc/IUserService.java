package cn.leafw.zone.gateway.rpc;

import cn.leafw.zone.common.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author carey
 * @date 2019/3/4
 */
@FeignClient(name = "zone-user")
@Component
public interface IUserService {

    @RequestMapping(value = "/checkUserLogin", method = RequestMethod.GET)
    ResponseDto checkUserLogin(@RequestParam("userName") String userName, @RequestParam("password") String password);
}
