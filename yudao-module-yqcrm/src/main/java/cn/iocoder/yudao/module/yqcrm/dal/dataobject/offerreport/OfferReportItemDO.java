package cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 要约报备物料明细 DO
 *
 * @author YUANQI
 */
@TableName("yqcrm_offer_report_item")
@KeySequence("yqcrm_offer_report_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferReportItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 要约报备编号
     */
    private Long offerReportId;
    /**
     * 物料编号
     */
    private Long materialId;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 规格
     */
    private String specification;
    /**
     * 型号
     */
    private String model;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 要约数量
     */
    private BigDecimal quantity;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 排序
     */
    private Integer sort;

}