package cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport.vo;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 要约报备分页 Request VO")
@Data
public class OfferReportPageReqVO extends PageParam {

    @Schema(description = "要约编号")
    private String no;

    @Schema(description = "要约名称", example = "李四")
    private String name;

    @Schema(description = "客户编号", example = "4679")
    private Long customerId;

    @Schema(description = "客户编码快照")
    private String customerNo;

    @Schema(description = "客户名称快照", example = "王五")
    private String customerName;

    @Schema(description = "联系人编号", example = "15876")
    private Long contactId;

    @Schema(description = "联系人姓名快照", example = "王五")
    private String contactName;

    @Schema(description = "联系人手机号快照")
    private String contactMobile;

    @Schema(description = "要约类型", example = "1")
    private Short type;

    @Schema(description = "要约日期，按月存储，取当月1日")
    private LocalDate offerMonth;

    @Schema(description = "交易方向")
    private Short tradeDirection;

    @Schema(description = "付款方式")
    private Short paymentMethod;

    @Schema(description = "交货期限")
    private LocalDate deliveryMonth;

    @Schema(description = "要约总数量")
    private BigDecimal totalQuantity;

    @Schema(description = "要约总金额")
    private BigDecimal totalAmount;

    @Schema(description = "归属销售用户编号", example = "26242")
    private Long ownerUserId;

    @Schema(description = "归属销售部门编号", example = "6562")
    private Long ownerDeptId;

    @Schema(description = "报备人用户编号", example = "11420")
    private Long reportUserId;

    @Schema(description = "报备人部门编号", example = "684")
    private Long reportDeptId;

    @Schema(description = "报备状态", example = "0")
    private Short status;

    @Schema(description = "BPM 流程实例编号", example = "5654")
    private String processInstanceId;

    @Schema(description = "最近跟进时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] latestFollowTime;

    @Schema(description = "最近跟进记录编号", example = "15688")
    private Long latestFollowRecordId;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}