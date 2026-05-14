package cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport.vo.*;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportDO;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportItemDO;
import cn.iocoder.yudao.module.yqcrm.service.offerreport.OfferReportService;

@Tag(name = "管理后台 - 要约报备")
@RestController
@RequestMapping("/yqcrm/offer-report")
@Validated
public class OfferReportController {

    @Resource
    private OfferReportService offerReportService;

    @PostMapping("/create")
    @Operation(summary = "创建要约报备")
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:create')")
    public CommonResult<Long> createOfferReport(@Valid @RequestBody OfferReportSaveReqVO createReqVO) {
        return success(offerReportService.createOfferReport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新要约报备")
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:update')")
    public CommonResult<Boolean> updateOfferReport(@Valid @RequestBody OfferReportSaveReqVO updateReqVO) {
        offerReportService.updateOfferReport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除要约报备")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:delete')")
    public CommonResult<Boolean> deleteOfferReport(@RequestParam("id") Long id) {
        offerReportService.deleteOfferReport(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除要约报备")
                @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:delete')")
    public CommonResult<Boolean> deleteOfferReportList(@RequestParam("ids") List<Long> ids) {
        offerReportService.deleteOfferReportListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得要约报备")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:query')")
    public CommonResult<OfferReportRespVO> getOfferReport(@RequestParam("id") Long id) {
        OfferReportDO offerReport = offerReportService.getOfferReport(id);
        return success(BeanUtils.toBean(offerReport, OfferReportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得要约报备分页")
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:query')")
    public CommonResult<PageResult<OfferReportRespVO>> getOfferReportPage(@Valid OfferReportPageReqVO pageReqVO) {
        PageResult<OfferReportDO> pageResult = offerReportService.getOfferReportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OfferReportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出要约报备 Excel")
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOfferReportExcel(@Valid OfferReportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OfferReportDO> list = offerReportService.getOfferReportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "要约报备.xls", "数据", OfferReportRespVO.class,
                        BeanUtils.toBean(list, OfferReportRespVO.class));
    }

    // ==================== 子表（要约报备物料明细） ====================

    @GetMapping("/offer-report-item/list-by-offer-report-id")
    @Operation(summary = "获得要约报备物料明细列表")
    @Parameter(name = "offerReportId", description = "要约报备编号")
    @PreAuthorize("@ss.hasPermission('yqcrm:offer-report:query')")
    public CommonResult<List<OfferReportItemDO>> getOfferReportItemListByOfferReportId(@RequestParam("offerReportId") Long offerReportId) {
        return success(offerReportService.getOfferReportItemListByOfferReportId(offerReportId));
    }

}