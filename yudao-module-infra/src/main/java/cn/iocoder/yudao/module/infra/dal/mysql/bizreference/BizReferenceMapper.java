package cn.iocoder.yudao.module.infra.dal.mysql.bizreference;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.bizreference.BizReferenceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BizReferenceMapper extends BaseMapperX<BizReferenceDO> {

    default BizReferenceDO selectByCode(String code) {
        return selectOne(new LambdaQueryWrapperX<BizReferenceDO>()
                .eq(BizReferenceDO::getCode, code)
                .eq(BizReferenceDO::getStatus, CommonStatusEnum.ENABLE.getStatus()));
    }

}
