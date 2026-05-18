package cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 业务参照分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BizReferencePageReqVO extends PageParam {

    @Schema(description = "参照编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "system-user")
    @NotBlank(message = "参照编码不能为空")
    private String code;

}
