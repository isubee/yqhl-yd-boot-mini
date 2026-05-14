package cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 要约报备 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OfferReportRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7853")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "要约编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("要约编号")
    private String no;

    @Schema(description = "要约名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("要约名称")
    private String name;

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4679")
    @ExcelProperty("客户编号")
    private Long customerId;

    @Schema(description = "客户编码快照", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("客户编码快照")
    private String customerNo;

    @Schema(description = "客户名称快照", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("客户名称快照")
    private String customerName;

    @Schema(description = "联系人编号", example = "15876")
    @ExcelProperty("联系人编号")
    private Long contactId;

    @Schema(description = "联系人姓名快照", example = "王五")
    @ExcelProperty("联系人姓名快照")
    private String contactName;

    @Schema(description = "联系人手机号快照")
    @ExcelProperty("联系人手机号快照")
    private String contactMobile;

    @Schema(description = "要约类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "要约类型", converter = DictConvert.class)
    @DictFormat("yq_crm_offer_report_type_enum") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Short type;

    @Schema(description = "要约日期，按月存储，取当月1日", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("要约日期，按月存储，取当月1日")
    private LocalDate offerMonth;

    @Schema(description = "交易方向", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "交易方向", converter = DictConvert.class)
    @DictFormat("yq_crm_offer_report_trade_direction_enum") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Short tradeDirection;

    @Schema(description = "付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "付款方式", converter = DictConvert.class)
    @DictFormat("yq_crm_offer_report_payment_method_enum") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Short paymentMethod;

    @Schema(description = "交货期限", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交货期限")
    private LocalDate deliveryMonth;

    @Schema(description = "要约总数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("要约总数量")
    private BigDecimal totalQuantity;

    @Schema(description = "要约总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("要约总金额")
    private BigDecimal totalAmount;

    @Schema(description = "归属销售用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "26242")
    @ExcelProperty("归属销售用户编号")
    private Long ownerUserId;

    @Schema(description = "归属销售部门编号", example = "6562")
    @ExcelProperty("归属销售部门编号")
    private Long ownerDeptId;

    @Schema(description = "报备人用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11420")
    @ExcelProperty("报备人用户编号")
    private Long reportUserId;

    @Schema(description = "报备人部门编号", example = "684")
    @ExcelProperty("报备人部门编号")
    private Long reportDeptId;

    @Schema(description = "报备状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty(value = "报备状态", converter = DictConvert.class)
    @DictFormat("yq_crm_offer_report_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Short status;

    @Schema(description = "BPM 流程实例编号", example = "5654")
    @ExcelProperty("BPM 流程实例编号")
    private String processInstanceId;

    @Schema(description = "最近跟进时间")
    @ExcelProperty("最近跟进时间")
    private LocalDateTime latestFollowTime;

    @Schema(description = "最近跟进记录编号", example = "15688")
    @ExcelProperty("最近跟进记录编号")
    private Long latestFollowRecordId;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
