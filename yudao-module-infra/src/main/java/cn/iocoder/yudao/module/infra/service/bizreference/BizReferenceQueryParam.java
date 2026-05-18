package cn.iocoder.yudao.module.infra.service.bizreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class BizReferenceQueryParam {

    private String tableName;
    private List<SelectField> selectFields;
    private List<SearchCondition> conditions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectField {
        private String column;
        private String alias;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCondition {
        private String column;
        private String operator;
        private Object value;
        private List<Object> values;
    }

}
