package cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 要约报备 DO
 *
 * @author YUANQI
 */
@TableName("yq_crm_offer_report")
@KeySequence("yq_crm_offer_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferReportDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 要约编号
     */
    private String no;
    /**
     * 要约名称
     */
    private String name;
    /**
     * 客户编号
     */
    private Long customerId;
    /**
     * 客户编码快照
     */
    private String customerNo;
    /**
     * 客户名称快照
     */
    private String customerName;
    /**
     * 联系人编号
     */
    private Long contactId;
    /**
     * 联系人姓名快照
     */
    private String contactName;
    /**
     * 联系人手机号快照
     */
    private String contactMobile;
    /**
     * 要约类型
     *
     * 枚举 {@link TODO yq_crm_offer_report_type_enum 对应的类}
     */
    private Short type;
    /**
     * 要约日期，按月存储，取当月1日
     */
    private LocalDate offerMonth;
    /**
     * 交易方向
     *
     * 枚举 {@link TODO yq_crm_offer_report_trade_direction_enum 对应的类}
     */
    private Short tradeDirection;
    /**
     * 付款方式
     *
     * 枚举 {@link TODO yq_crm_offer_report_payment_method_enum 对应的类}
     */
    private Short paymentMethod;
    /**
     * 交货期限
     */
    private LocalDate deliveryMonth;
    /**
     * 要约总数量
     */
    private BigDecimal totalQuantity;
    /**
     * 要约总金额
     */
    private BigDecimal totalAmount;
    /**
     * 归属销售用户编号
     */
    private Long ownerUserId;
    /**
     * 归属销售部门编号
     */
    private Long ownerDeptId;
    /**
     * 报备人用户编号
     */
    private Long reportUserId;
    /**
     * 报备人部门编号
     */
    private Long reportDeptId;
    /**
     * 报备状态
     *
     * 枚举 {@link TODO yq_crm_offer_report_status 对应的类}
     */
    private Short status;
    /**
     * BPM 流程实例编号
     */
    private String processInstanceId;
    /**
     * 最近跟进时间
     */
    private LocalDateTime latestFollowTime;
    /**
     * 最近跟进记录编号
     */
    private Long latestFollowRecordId;
    /**
     * 备注
     */
    private String remark;


}