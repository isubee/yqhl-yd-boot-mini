package cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 业务参照配置 Response VO")
@Data
public class BizReferenceConfigRespVO {

    @Schema(description = "参照编码", example = "system-user")
    private String code;

    @Schema(description = "参照名称", example = "系统用户")
    private String name;

    @Schema(description = "值字段", example = "id")
    private String valueField;

    @Schema(description = "展示字段", example = "nickname")
    private String labelField;

    @Schema(description = "行主键字段", example = "id")
    private String rowKey;

    @Schema(description = "默认分页大小", example = "10")
    private Integer pageSize;

    @Schema(description = "表格列")
    private List<BizReferenceFieldRespVO> columns;

    @Schema(description = "搜索字段")
    private List<BizReferenceFieldRespVO> searchSchema;

    @Schema(description = "回写字段映射")
    private Map<String, String> returnFields;

}
