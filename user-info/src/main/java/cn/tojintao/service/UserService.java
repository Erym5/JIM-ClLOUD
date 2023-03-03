package cn.tojintao.service;

import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.model.dto.UserDTO;
import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/19 16:47
 */
public interface UserService {
    UserDTO loadloadUserByUsername(@RequestParam("userName") String userName);

    ResultInfo<JSONObject> login(String userName, String password) throws Exception;

    ResultInfo<JSONObject> refreshAccessToken(String refreshToken) throws Exception;

    ResultInfo<User> findUserById(Integer userId);

    ResultInfo<List<User>> getUserByIdList(List<Integer> userIdList);

    ResultInfo<List<Group>> getAllGroup(Integer userId);

    ResultInfo<List<Integer>> getGroupUser(Integer groupId);

    ResultInfo<Group> getGroupById(Integer groupId);

    ResultInfo<String> isBan(Integer userId);
}
