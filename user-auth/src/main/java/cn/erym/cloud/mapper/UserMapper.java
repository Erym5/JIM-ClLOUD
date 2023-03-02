package cn.erym.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.erym.cloud.domain.entity.UserEntity;
import cn.erym.cloud.domain.model.CasbinRoleResourceModel;
import cn.erym.cloud.domain.model.CasbinUserResourcePermissionModel;
import cn.erym.cloud.domain.model.CasbinUserRoleModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    List<CasbinUserResourcePermissionModel> getCasbinUserResourcePermissionModel();

    List<CasbinUserRoleModel> getCasbinUserRoleModel();

    List<CasbinRoleResourceModel> getCasbinRoleResourceModel();


    List<CasbinUserResourcePermissionModel> getCasbinUserResourcePermissionModelByUserIdAndRoleId(Integer userId, Integer roleId);
}
