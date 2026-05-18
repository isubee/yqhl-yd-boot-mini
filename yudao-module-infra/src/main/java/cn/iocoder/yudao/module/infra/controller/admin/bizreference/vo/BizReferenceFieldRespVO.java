package cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 业务参照字段 Response VO")
@Data
public class BizReferenceFieldRespVO {

    @Schema(description = "字段名", example = "nickname")
    private String field;

    @Schema(description = "表格标题", example = "员工姓名")
    private String title;

    @Schema(description = "搜索标签", example = "员工姓名")
    private String label;

    @Schema(description = "搜索组件", example = "Input")
    private String component;

    @Schema(description = "字典类型", example = "system_user_type")
    private String dictType;

    @Schema(description = "列宽", example = "160")
    private String width;

}
