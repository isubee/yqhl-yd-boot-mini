package cn.iocoder.yudao.module.infra.dal.mysql.bizreference;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.bizreference.BizReferenceFieldDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BizReferenceFieldMapper extends BaseMapperX<BizReferenceFieldDO> {

    default List<BizReferenceFieldDO> selectListByReferenceId(Long referenceId) {
        return selectList(new LambdaQueryWrapperX<BizReferenceFieldDO>()
                .eq(BizReferenceFieldDO::getReferenceId, referenceId)
                .eq(BizReferenceFieldDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(BizReferenceFieldDO::getSort));
    }

}
