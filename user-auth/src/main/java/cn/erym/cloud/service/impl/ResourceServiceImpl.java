package cn.erym.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.erym.cloud.domain.entity.ResourceEntity;
import cn.erym.cloud.mapper.ResourceMapper;
import cn.erym.cloud.service.IResourceService;
import org.springframework.stereotype.Service;

/**
 * =====================================================================================
 *
 * @Created :   2021/11/20 20:43:07
 * 
 * @Author :    VINO
 * @Copyright : VINO
 * @Decription :  服务实现类
 * =====================================================================================
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourceEntity> implements IResourceService {

}
