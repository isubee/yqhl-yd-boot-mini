package cn.iocoder.yudao.module.yqcrm.dal.mysql.offerreport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.yqcrm.controller.admin.offerreport.vo.*;

/**
 * 要约报备 Mapper
 *
 * @author YUANQI
 */
@Mapper
public interface OfferReportMapper extends BaseMapperX<OfferReportDO> {

    default PageResult<OfferReportDO> selectPage(OfferReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OfferReportDO>()
                .eqIfPresent(OfferReportDO::getNo, reqVO.getNo())
                .likeIfPresent(OfferReportDO::getName, reqVO.getName())
                .eqIfPresent(OfferReportDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(OfferReportDO::getCustomerNo, reqVO.getCustomerNo())
                .likeIfPresent(OfferReportDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(OfferReportDO::getContactId, reqVO.getContactId())
                .likeIfPresent(OfferReportDO::getContactName, reqVO.getContactName())
                .eqIfPresent(OfferReportDO::getContactMobile, reqVO.getContactMobile())
                .eqIfPresent(OfferReportDO::getType, reqVO.getType())
                .eqIfPresent(OfferReportDO::getOfferMonth, reqVO.getOfferMonth())
                .eqIfPresent(OfferReportDO::getTradeDirection, reqVO.getTradeDirection())
                .eqIfPresent(OfferReportDO::getPaymentMethod, reqVO.getPaymentMethod())
                .eqIfPresent(OfferReportDO::getDeliveryMonth, reqVO.getDeliveryMonth())
                .eqIfPresent(OfferReportDO::getTotalQuantity, reqVO.getTotalQuantity())
                .eqIfPresent(OfferReportDO::getTotalAmount, reqVO.getTotalAmount())
                .eqIfPresent(OfferReportDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(OfferReportDO::getOwnerDeptId, reqVO.getOwnerDeptId())
                .eqIfPresent(OfferReportDO::getReportUserId, reqVO.getReportUserId())
                .eqIfPresent(OfferReportDO::getReportDeptId, reqVO.getReportDeptId())
                .eqIfPresent(OfferReportDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OfferReportDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(OfferReportDO::getLatestFollowTime, reqVO.getLatestFollowTime())
                .eqIfPresent(OfferReportDO::getLatestFollowRecordId, reqVO.getLatestFollowRecordId())
                .eqIfPresent(OfferReportDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(OfferReportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OfferReportDO::getId));
    }

}