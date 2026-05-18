package cn.iocoder.yudao.module.infra.dal.dataobject.bizreference;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 业务参照配置
 */
@TableName("infra_biz_reference")
@KeySequence("infra_biz_reference_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class BizReferenceDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 参照编码
     */
    private String code;
    /**
     * 参照名称
     */
    private String name;
    /**
     * 物理表名
     */
    private String tableName;
    /**
     * 值字段
     */
    private String valueField;
    /**
     * 展示字段
     */
    private String labelField;
    /**
     * 行主键字段
     */
    private String rowKey;
    /**
     * 默认分页大小
     */
    private Integer defaultPageSize;
    /**
     * 参照级权限
     */
    private String permission;
    /**
     * 租户字段
     */
    private String tenantColumn;
    /**
     * 逻辑删除字段
     */
    private String deletedColumn;
    /**
     * 未删除值
     */
    private String deletedValue;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
