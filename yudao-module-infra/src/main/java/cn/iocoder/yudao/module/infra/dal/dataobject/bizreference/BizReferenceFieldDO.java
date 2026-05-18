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
 * 业务参照字段配置
 */
@TableName("infra_biz_reference_field")
@KeySequence("infra_biz_reference_field_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class BizReferenceFieldDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 参照配置编号
     */
    private Long referenceId;
    /**
     * 数据库字段名
     */
    private String fieldName;
    /**
     * 返回别名
     */
    private String aliasName;
    /**
     * 展示名称
     */
    private String label;
    /**
     * 是否表格列
     */
    private Boolean listVisible;
    /**
     * 是否搜索字段
     */
    private Boolean searchVisible;
    /**
     * 是否回写字段
     */
    private Boolean returnVisible;
    /**
     * 回写表单字段
     */
    private String returnField;
    /**
     * 搜索组件
     */
    private String searchComponent;
    /**
     * 搜索操作符
     */
    private String searchOperator;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 列宽
     */
    private String width;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     */
    private Integer status;

}
