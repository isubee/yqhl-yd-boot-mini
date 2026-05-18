package cn.iocoder.yudao.module.infra.service.bizreference;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.service.SecurityFrameworkService;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferenceConfigRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferenceFieldRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.bizreference.vo.BizReferencePageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.bizreference.BizReferenceDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.bizreference.BizReferenceFieldDO;
import cn.iocoder.yudao.module.infra.dal.mysql.bizreference.BizReferenceFieldMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.bizreference.BizReferenceMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.bizreference.BizReferenceQueryMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.BIZ_REFERENCE_INVALID_IDENTIFIER;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.BIZ_REFERENCE_NOT_EXISTS;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.BIZ_REFERENCE_PERMISSION_DENIED;

@Service
@Validated
public class BizReferenceServiceImpl implements BizReferenceService {

    private static final List<String> PAGE_PARAM_NAMES = Arrays.asList("code", "pageNo", "pageSize");

    @Resource
    private BizReferenceMapper bizReferenceMapper;
    @Resource
    private BizReferenceFieldMapper bizReferenceFieldMapper;
    @Resource
    private BizReferenceQueryMapper bizReferenceQueryMapper;
    @Resource
    private SecurityFrameworkService securityFrameworkService;

    @Override
    public BizReferenceConfigRespVO getBizReferenceConfig(String code) {
        BizReferenceDO reference = getEnabledReference(code);
        List<BizReferenceFieldDO> fields = getEnabledFields(reference);

        BizReferenceConfigRespVO respVO = new BizReferenceConfigRespVO();
        respVO.setCode(reference.getCode());
        respVO.setName(reference.getName());
        respVO.setValueField(reference.getValueField());
        respVO.setLabelField(reference.getLabelField());
        respVO.setRowKey(StrUtil.blankToDefault(reference.getRowKey(), reference.getValueField()));
        respVO.setPageSize(reference.getDefaultPageSize());
        respVO.setColumns(fields.stream().filter(field -> Boolean.TRUE.equals(field.getListVisible()))
                .map(this::buildFieldRespVO).collect(Collectors.toList()));
        respVO.setSearchSchema(fields.stream().filter(field -> Boolean.TRUE.equals(field.getSearchVisible()))
                .map(this::buildFieldRespVO).collect(Collectors.toList()));
        respVO.setReturnFields(buildReturnFields(fields));
        return respVO;
    }

    @Override
    public PageResult<Map<String, Object>> getBizReferencePage(BizReferencePageReqVO pageReqVO,
                                                               Map<String, String[]> requestParams) {
        BizReferenceDO reference = getEnabledReference(pageReqVO.getCode());
        validatePermission(reference);
        List<BizReferenceFieldDO> fields = getEnabledFields(reference);
        BizReferenceQueryParam queryParam = buildQueryParam(reference, fields, requestParams);

        Page<Map<String, Object>> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        List<Map<String, Object>> list = bizReferenceQueryMapper.selectPage(page, queryParam);
        return new PageResult<>(normalizeRows(list, queryParam.getSelectFields()), page.getTotal());
    }

    private BizReferenceDO getEnabledReference(String code) {
        BizReferenceDO reference = bizReferenceMapper.selectByCode(code);
        if (reference == null) {
            throw exception(BIZ_REFERENCE_NOT_EXISTS);
        }
        List<BizReferenceFieldDO> fields = bizReferenceFieldMapper.selectListByReferenceId(reference.getId());
        validateReference(reference, fields);
        return reference;
    }

    private List<BizReferenceFieldDO> getEnabledFields(BizReferenceDO reference) {
        return bizReferenceFieldMapper.selectListByReferenceId(reference.getId());
    }

    private void validatePermission(BizReferenceDO reference) {
        if (StrUtil.isNotBlank(reference.getPermission()) && !securityFrameworkService.hasPermission(reference.getPermission())) {
            throw exception(BIZ_REFERENCE_PERMISSION_DENIED);
        }
    }

    private List<Map<String, Object>> normalizeRows(List<Map<String, Object>> rows,
                                                    List<BizReferenceQueryParam.SelectField> selectFields) {
        rows.forEach(row -> selectFields.forEach(field -> {
            String alias = field.getAlias();
            if (!row.containsKey(alias) && row.containsKey(alias.toUpperCase())) {
                row.put(alias, row.get(alias.toUpperCase()));
            }
        }));
        return rows;
    }

    private BizReferenceQueryParam buildQueryParam(BizReferenceDO reference, List<BizReferenceFieldDO> fields,
                                                   Map<String, String[]> requestParams) {
        BizReferenceQueryParam queryParam = new BizReferenceQueryParam();
        queryParam.setTableName(reference.getTableName());
        queryParam.setSelectFields(fields.stream()
                .map(field -> new BizReferenceQueryParam.SelectField(field.getFieldName(), getAliasName(field)))
                .collect(Collectors.toList()));
        queryParam.setConditions(buildConditions(reference, fields, requestParams));
        return queryParam;
    }

    private List<BizReferenceQueryParam.SearchCondition> buildConditions(BizReferenceDO reference,
                                                                         List<BizReferenceFieldDO> fields,
                                                                         Map<String, String[]> requestParams) {
        List<BizReferenceQueryParam.SearchCondition> conditions = new ArrayList<>();
        fields.stream().filter(field -> Boolean.TRUE.equals(field.getSearchVisible()))
                .forEach(field -> addSearchCondition(conditions, field, requestParams));
        Long tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isNotBlank(reference.getTenantColumn()) && tenantId != null) {
            conditions.add(new BizReferenceQueryParam.SearchCondition(reference.getTenantColumn(), "eq", tenantId, null));
        }
        if (StrUtil.isAllNotBlank(reference.getDeletedColumn(), reference.getDeletedValue())) {
            conditions.add(new BizReferenceQueryParam.SearchCondition(reference.getDeletedColumn(), "eq", reference.getDeletedValue(), null));
        }
        return conditions;
    }

    private void addSearchCondition(List<BizReferenceQueryParam.SearchCondition> conditions, BizReferenceFieldDO field,
                                    Map<String, String[]> requestParams) {
        String[] values = requestParams.get(getAliasName(field));
        if (values == null || values.length == 0 || StrUtil.isBlank(values[0]) || PAGE_PARAM_NAMES.contains(getAliasName(field))) {
            return;
        }
        String operator = field.getSearchOperator();
        if ("like".equals(operator)) {
            conditions.add(new BizReferenceQueryParam.SearchCondition(field.getFieldName(), operator, "%" + values[0] + "%", null));
        } else if ("in".equals(operator)) {
            List<Object> inValues = Arrays.stream(values[0].split(","))
                    .filter(StrUtil::isNotBlank).map(String::trim).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(inValues)) {
                conditions.add(new BizReferenceQueryParam.SearchCondition(field.getFieldName(), operator, null, inValues));
            }
        } else if ("between".equals(operator)) {
            List<Object> betweenValues = Arrays.stream(values.length >= 2 ? values : values[0].split(","))
                    .filter(StrUtil::isNotBlank).map(String::trim).collect(Collectors.toList());
            if (betweenValues.size() == 2) {
                conditions.add(new BizReferenceQueryParam.SearchCondition(field.getFieldName(), operator, null, betweenValues));
            }
        } else {
            conditions.add(new BizReferenceQueryParam.SearchCondition(field.getFieldName(), operator, values[0], null));
        }
    }

    private void validateReference(BizReferenceDO reference, List<BizReferenceFieldDO> fields) {
        validateIdentifier(reference.getTableName());
        validateIdentifier(reference.getValueField());
        validateIdentifier(reference.getLabelField());
        validateIdentifier(StrUtil.blankToDefault(reference.getRowKey(), reference.getValueField()));
        validateIdentifierIfPresent(reference.getTenantColumn());
        validateIdentifierIfPresent(reference.getDeletedColumn());
        fields.forEach(field -> {
            validateIdentifier(field.getFieldName());
            validateIdentifier(getAliasName(field));
            validateIdentifierIfPresent(field.getReturnField());
            if (Boolean.TRUE.equals(field.getSearchVisible())
                    && !BizReferenceSqlUtils.isSupportedOperator(field.getSearchOperator())) {
                throw exception(BIZ_REFERENCE_INVALID_IDENTIFIER);
            }
        });
    }

    private void validateIdentifier(String identifier) {
        if (!BizReferenceSqlUtils.isValidIdentifier(identifier)) {
            throw exception(BIZ_REFERENCE_INVALID_IDENTIFIER);
        }
    }

    private void validateIdentifierIfPresent(String identifier) {
        if (StrUtil.isNotBlank(identifier)) {
            validateIdentifier(identifier);
        }
    }

    private BizReferenceFieldRespVO buildFieldRespVO(BizReferenceFieldDO field) {
        BizReferenceFieldRespVO respVO = new BizReferenceFieldRespVO();
        respVO.setField(getAliasName(field));
        respVO.setTitle(StrUtil.blankToDefault(field.getLabel(), getAliasName(field)));
        respVO.setLabel(StrUtil.blankToDefault(field.getLabel(), getAliasName(field)));
        respVO.setComponent(StrUtil.blankToDefault(field.getSearchComponent(), "Input"));
        respVO.setDictType(field.getDictType());
        respVO.setWidth(field.getWidth());
        return respVO;
    }

    private Map<String, String> buildReturnFields(List<BizReferenceFieldDO> fields) {
        Map<String, String> returnFields = new LinkedHashMap<>();
        fields.stream().filter(field -> Boolean.TRUE.equals(field.getReturnVisible()))
                .forEach(field -> returnFields.put(StrUtil.blankToDefault(field.getReturnField(), getAliasName(field)), getAliasName(field)));
        return returnFields;
    }

    private String getAliasName(BizReferenceFieldDO field) {
        return StrUtil.blankToDefault(field.getAliasName(), field.getFieldName());
    }

}
