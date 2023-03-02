package cn.erym.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.erym.cloud.domain.entity.UserEntity;
import cn.erym.cloud.domain.model.CasbinRoleResourceModel;
import cn.erym.cloud.domain.model.CasbinUserResourcePermissionModel;
import cn.erym.cloud.domain.model.CasbinUserRoleModel;

import java.util.List;


public interface IUserService extends IService<UserEntity> {

    List<CasbinUserResourcePermissionModel> getCasbinUserResourcePermissionModel();

    List<CasbinUserRoleModel> getCasbinUserRoleModel();

    List<CasbinRoleResourceModel> getCasbinRoleResourceModel();

    Boolean allocateRole(Integer userId, Integer roleId);

    Boolean allocateRoleResource(Integer roleId, Integer resourceId);

    List<CasbinUserResourcePermissionModel> getCasbinUserResourcePermissionModel(Integer userId, Integer roleId);
}
