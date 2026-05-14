package cn.iocoder.yudao.module.yqcrm.service.offerreport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport.vo.*;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportDO;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 要约报备 Service 接口
 *
 * @author YUANQI
 */
public interface OfferReportService {

    /**
     * 创建要约报备
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOfferReport(@Valid OfferReportSaveReqVO createReqVO);

    /**
     * 更新要约报备
     *
     * @param updateReqVO 更新信息
     */
    void updateOfferReport(@Valid OfferReportSaveReqVO updateReqVO);

    /**
     * 删除要约报备
     *
     * @param id 编号
     */
    void deleteOfferReport(Long id);

    /**
    * 批量删除要约报备
    *
    * @param ids 编号
    */
    void deleteOfferReportListByIds(List<Long> ids);

    /**
     * 获得要约报备
     *
     * @param id 编号
     * @return 要约报备
     */
    OfferReportDO getOfferReport(Long id);

    /**
     * 获得要约报备分页
     *
     * @param pageReqVO 分页查询
     * @return 要约报备分页
     */
    PageResult<OfferReportDO> getOfferReportPage(OfferReportPageReqVO pageReqVO);

    // ==================== 子表（要约报备物料明细） ====================

    /**
     * 获得要约报备物料明细列表
     *
     * @param offerReportId 要约报备编号
     * @return 要约报备物料明细列表
     */
    List<OfferReportItemDO> getOfferReportItemListByOfferReportId(Long offerReportId);

}