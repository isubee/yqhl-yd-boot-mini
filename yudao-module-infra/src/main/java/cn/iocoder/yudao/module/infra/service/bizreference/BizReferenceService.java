package cn.iocoder.yudao.module.infra.service.bizreference;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferenceConfigRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferencePageReqVO;

import java.util.Map;

public interface BizReferenceService {

    BizReferenceConfigRespVO getBizReferenceConfig(String code);

    PageResult<Map<String, Object>> getBizReferencePage(BizReferencePageReqVO pageReqVO, Map<String, String[]> requestParams);

}
