package cn.erym.cloud.feign;

import cn.tojintao.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-info")
public interface UserInfoService {

    @GetMapping("/user-info/user/loadUserByUsername")
    UserDTO loadUserByUsername(@RequestParam("userName") String userName);
}