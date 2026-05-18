package cn.iocoder.yudao.module.infra.dal.mysql.bizreference;

import cn.iocoder.yudao.module.infra.service.bizreference.BizReferenceQueryParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BizReferenceQueryMapper {

    List<Map<String, Object>> selectPage(IPage<Map<String, Object>> page, @Param("query") BizReferenceQueryParam query);

}
