package cn.iocoder.yudao.module.infra.controller.admin.bizreference;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferenceConfigRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferencePageReqVO;
import cn.iocoder.yudao.module.infra.service.bizreference.BizReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 业务参照")
@RestController
@RequestMapping("/infra/biz-reference")
@Validated
public class BizReferenceController {

    @Resource
    private BizReferenceService bizReferenceService;

    @GetMapping("/config")
    @Operation(summary = "获得业务参照配置")
    @Parameter(name = "code", description = "参照编码", required = true, example = "system-user")
    @PreAuthorize("@ss.hasPermission('infra:biz-reference:query')")
    public CommonResult<BizReferenceConfigRespVO> getBizReferenceConfig(@RequestParam("code") String code) {
        return success(bizReferenceService.getBizReferenceConfig(code));
    }

    @GetMapping("/page")
    @Operation(summary = "获得业务参照分页")
    @PreAuthorize("@ss.hasPermission('infra:biz-reference:query')")
    public CommonResult<PageResult<Map<String, Object>>> getBizReferencePage(@Valid BizReferencePageReqVO pageReqVO,
                                                                             HttpServletRequest request) {
        return success(bizReferenceService.getBizReferencePage(pageReqVO, request.getParameterMap()));
    }

}
