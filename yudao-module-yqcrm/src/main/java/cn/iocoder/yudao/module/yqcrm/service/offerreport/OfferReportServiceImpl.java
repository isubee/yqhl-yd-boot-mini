package cn.iocoder.yudao.module.yqcrm.service.offerreport;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport.vo.*;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportDO;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.yqcrm.dal.mysql.offerreport.OfferReportMapper;
import cn.iocoder.yudao.module.yqcrm.dal.mysql.offerreport.OfferReportItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.yqcrm.enums.ErrorCodeConstants.*;

/**
 * 要约报备 Service 实现类
 *
 * @author YUANQI
 */
@Service
@Validated
public class OfferReportServiceImpl implements OfferReportService {

    @Resource
    private OfferReportMapper offerReportMapper;
    @Resource
    private OfferReportItemMapper offerReportItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOfferReport(OfferReportSaveReqVO createReqVO) {
        // 插入
        OfferReportDO offerReport = BeanUtils.toBean(createReqVO, OfferReportDO.class);
        offerReportMapper.insert(offerReport);


        // 插入子表
        createOfferReportItemList(offerReport.getId(), createReqVO.getOfferReportItems());
        // 返回
        return offerReport.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOfferReport(OfferReportSaveReqVO updateReqVO) {
        // 校验存在
        validateOfferReportExists(updateReqVO.getId());
        // 更新
        OfferReportDO updateObj = BeanUtils.toBean(updateReqVO, OfferReportDO.class);
        offerReportMapper.updateById(updateObj);

        // 更新子表
        updateOfferReportItemList(updateReqVO.getId(), updateReqVO.getOfferReportItems());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOfferReport(Long id) {
        // 校验存在
        validateOfferReportExists(id);
        // 删除
        offerReportMapper.deleteById(id);

        // 删除子表
        deleteOfferReportItemByOfferReportId(id);
    }

    @Override
        @Transactional(rollbackFor = Exception.class)
    public void deleteOfferReportListByIds(List<Long> ids) {
        // 删除
        offerReportMapper.deleteByIds(ids);
    
    // 删除子表
            deleteOfferReportItemByOfferReportIds(ids);
    }


    private void validateOfferReportExists(Long id) {
        if (offerReportMapper.selectById(id) == null) {
            throw exception(OFFER_REPORT_NOT_EXISTS);
        }
    }

    @Override
    public OfferReportDO getOfferReport(Long id) {
        return offerReportMapper.selectById(id);
    }

    @Override
    public PageResult<OfferReportDO> getOfferReportPage(OfferReportPageReqVO pageReqVO) {
        return offerReportMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（要约报备物料明细） ====================

    @Override
    public List<OfferReportItemDO> getOfferReportItemListByOfferReportId(Long offerReportId) {
        return offerReportItemMapper.selectListByOfferReportId(offerReportId);
    }

    private void createOfferReportItemList(Long offerReportId, List<OfferReportItemDO> list) {
        list.forEach(o -> o.setOfferReportId(offerReportId).clean());
        offerReportItemMapper.insertBatch(list);
    }

    private void updateOfferReportItemList(Long offerReportId, List<OfferReportItemDO> list) {
	    list.forEach(o -> o.setOfferReportId(offerReportId).clean());
	    List<OfferReportItemDO> oldList = offerReportItemMapper.selectListByOfferReportId(offerReportId);
	    List<List<OfferReportItemDO>> diffList = diffList(oldList, list, (oldVal, newVal) -> {
            boolean same = ObjectUtil.equal(oldVal.getId(), newVal.getId());
            if (same) {
                newVal.setId(oldVal.getId()).clean(); // 解决更新情况下：updateTime 不更新
            }
            return same;
	    });

	    // 第二步，批量添加、修改、删除
	    if (CollUtil.isNotEmpty(diffList.get(0))) {
	        offerReportItemMapper.insertBatch(diffList.get(0));
	    }
	    if (CollUtil.isNotEmpty(diffList.get(1))) {
	        offerReportItemMapper.updateBatch(diffList.get(1));
	    }
	    if (CollUtil.isNotEmpty(diffList.get(2))) {
	        offerReportItemMapper.deleteByIds(convertList(diffList.get(2), OfferReportItemDO::getId));
	    }
    }

    private void deleteOfferReportItemByOfferReportId(Long offerReportId) {
        offerReportItemMapper.deleteByOfferReportId(offerReportId);
    }

	private void deleteOfferReportItemByOfferReportIds(List<Long> offerReportIds) {
        offerReportItemMapper.deleteByOfferReportIds(offerReportIds);
	}

}