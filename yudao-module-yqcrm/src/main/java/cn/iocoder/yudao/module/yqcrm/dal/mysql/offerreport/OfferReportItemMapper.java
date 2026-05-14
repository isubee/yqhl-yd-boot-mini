package cn.iocoder.yudao.module.yqcrm.dal.mysql.offerreport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.yqcrm.dal.dataobject.offerreport.OfferReportItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 要约报备物料明细 Mapper
 *
 * @author YUANQI
 */
@Mapper
public interface OfferReportItemMapper extends BaseMapperX<OfferReportItemDO> {

    default List<OfferReportItemDO> selectListByOfferReportId(Long offerReportId) {
        return selectList(OfferReportItemDO::getOfferReportId, offerReportId);
    }

    default int deleteByOfferReportId(Long offerReportId) {
        return delete(OfferReportItemDO::getOfferReportId, offerReportId);
    }

	default int deleteByOfferReportIds(List<Long> offerReportIds) {
	    return deleteBatch(OfferReportItemDO::getOfferReportId, offerReportIds);
	}

}