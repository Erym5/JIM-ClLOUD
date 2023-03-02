package cn.erym.cloud.service;

import cn.erym.cloud.feign.UserInfoService;
import cn.tojintao.constant.MessageConstant;
import cn.erym.cloud.domain.model.SecurityUser;
import cn.tojintao.model.dto.UserDTO;
import cn.tojintao.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理业务类
user
 */
@Service
public class UserServiceImpl2 implements UserDetailsService {

    private List<UserDTO> userList;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HttpServletRequest request;

//    @PostConstruct
//    public void initData() {
//        String password = passwordEncoder.encode("123456");
//        userList = new ArrayList<>();
//        userList.add(new UserDTO(1L,"macro", password,1, CollUtil.toList("ADMIN")));
//        userList.add(new UserDTO(2L,"andy", password,1, CollUtil.toList("TEST")));
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        List<UserDTO> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
//        if (CollUtil.isEmpty(findUserList)) {
//            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
//        }
//        SecurityUser securityUser = new SecurityUser(findUserList.get(0));
//        if (!securityUser.isEnabled()) {
//            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
//        } else if (!securityUser.isAccountNonLocked()) {
//            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
//        } else if (!securityUser.isAccountNonExpired()) {
//            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
//        } else if (!securityUser.isCredentialsNonExpired()) {
//            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
//        }
//        return securityUser;
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter("client_id");
        UserDTO userDto = null;
        if(AuthConstant.PORTAL_CLIENT_ID.equals(clientId)){
            userDto = userInfoService.loadUserByUsername(username);
        }
        if (userDto==null) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        userDto.setClientId(clientId);
        SecurityUser securityUser = new SecurityUser(userDto);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }

}
