package cn.erym.cloud.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * =====================================================================================
 *
 * @Created :   2021/11/20 14:46:27
 * 
 * @Author :    VINO
 * @Copyright : VINO
 * @Decription : 
 * =====================================================================================
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;

    private String loginName;

    private String passwd;

    private String realName;

    private LocalDateTime  createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Boolean status;


}
