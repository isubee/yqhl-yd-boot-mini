# 后端 SDD：通用业务参照接口

## 1. Objective

### 1.1 背景

前端已具备 `ApiTableSelect` 表格弹窗选择组件，用于后台表单中的大数据量业务参照选择。当前组件需要每个业务对象单独传入 `api(params)`、列配置、搜索字段和回写字段，仍然要求业务前端编写较多重复代码。

本后端 SDD 设计一个配置表驱动的通用业务参照接口，让前端只传 `referenceCode` 即可获取参照元数据与分页数据。

### 1.2 目标用户

- 管理后台业务开发者：在表单中配置参照字段，例如选择用户、客户、商品、项目等。
- 系统管理员或实施人员：后续可通过配置维护参照定义；首版可先通过 SQL 初始化配置。

### 1.3 目标能力

1. 按参照编码读取元数据：列、搜索字段、值字段、展示字段、回写字段。
2. 按参照编码分页查询业务数据。
3. 支持配置化搜索字段，首版操作符包含 `eq`、`like`、`in`、`between`。
4. 支持参照级权限校验。
5. 支持租户过滤、逻辑删除过滤。
6. 不允许前端传表名、字段名或 SQL 片段。
7. 返回结构遵循现有芋道约定：`CommonResult` + `PageResult` / VO。

### 1.4 非目标

首版不实现：

- 参照配置后台 CRUD 页面。
- 任意 SQL、join SQL、子查询 SQL 配置。
- 树形参照、左树右表、级联参照。
- 移动端或外部系统开放接口。
- 自动生成业务模块专属 Controller。

## 2. Commands

后端默认工作目录：

```bash
D:\yqhl-yd\yqhl-yd-boot-mini
```

相关测试：

```bash
mvn -pl yudao-module-infra -am test
```

如涉及 server 装配或全量编译：

```bash
mvn -pl yudao-server -am package -DskipTests
```

如全量命令失败且失败点与本任务无关，需要记录失败文件和错误，不顺手修改无关模块。

## 3. Project Structure

### 3.1 参照实现

实现风格参考：

- 分页 Controller：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/controller/admin/definition/BpmUserGroupController.java`
- 分页 VO：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/controller/admin/definition/vo/group/BpmUserGroupPageReqVO.java`
- Infra 模块现有 Controller、Service、Mapper、DO 命名与目录结构。

### 3.2 新增后端文件

```text
yudao-module-infra/src/main/java/cn/iocoder/yudao/module/infra/controller/admin/bizreference/
  BizReferenceController.java
  vo/
    BizReferenceConfigRespVO.java
    BizReferenceFieldRespVO.java
    BizReferencePageReqVO.java

yudao-module-infra/src/main/java/cn/iocoder/yudao/module/infra/service/bizreference/
  BizReferenceService.java
  BizReferenceServiceImpl.java

yudao-module-infra/src/main/java/cn/iocoder/yudao/module/infra/dal/dataobject/bizreference/
  BizReferenceDO.java
  BizReferenceFieldDO.java

yudao-module-infra/src/main/java/cn/iocoder/yudao/module/infra/dal/mysql/bizreference/
  BizReferenceMapper.java
  BizReferenceFieldMapper.java
  BizReferenceQueryMapper.java

yudao-module-infra/src/main/resources/mapper/bizreference/
  BizReferenceQueryMapper.xml
```

### 3.3 可能修改的现有文件

```text
yudao-module-infra/src/main/java/cn/iocoder/yudao/module/infra/enums/ErrorCodeConstants.java
```

新增错误码时必须避开已有错误码区间。

### 3.4 数据库表

#### infra_biz_reference

| 字段 | 类型建议 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| code | varchar(64) | 参照编码，唯一，例如 `system-user` |
| name | varchar(128) | 参照名称 |
| table_name | varchar(128) | 物理表名，只能后端配置维护 |
| value_field | varchar(64) | 值字段 |
| label_field | varchar(64) | 展示字段 |
| row_key | varchar(64) | 表格行 key，默认等于 `value_field` |
| default_page_size | int | 默认分页大小 |
| permission | varchar(128) | 参照级权限，例如 `system:user:query`，可为空 |
| tenant_column | varchar(64) | 租户字段名，可为空 |
| deleted_column | varchar(64) | 逻辑删除字段名，可为空 |
| deleted_value | varchar(32) | 未删除值，例如 `0` |
| status | tinyint | 启用状态 |
| remark | varchar(512) | 备注 |
| creator/create_time/updater/update_time/deleted | 按项目约定 | 基础字段 |

#### infra_biz_reference_field

| 字段 | 类型建议 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| reference_id | bigint | 参照配置 ID |
| field_name | varchar(64) | 数据库字段名 |
| alias_name | varchar(64) | 返回给前端的字段名，默认等于字段名 |
| label | varchar(128) | 前端显示名 |
| list_visible | bit/tinyint | 是否作为表格列 |
| search_visible | bit/tinyint | 是否作为搜索字段 |
| return_visible | bit/tinyint | 是否作为回写字段 |
| search_component | varchar(32) | 首版支持 `Input`、`Select`、`DatePicker` |
| search_operator | varchar(16) | `eq`、`like`、`in`、`between` |
| dict_type | varchar(128) | 字典类型，可为空 |
| width | varchar(32) | 表格列宽 |
| sort | int | 排序 |
| status | tinyint | 启用状态 |
| creator/create_time/updater/update_time/deleted | 按项目约定 | 基础字段 |

## 4. API Contract

### 4.1 获取参照配置

```http
GET /admin-api/infra/biz-reference/config?code={referenceCode}
```

Controller 路径：

```java
@RequestMapping("/infra/biz-reference")
```

方法签名建议：

```java
@GetMapping("/config")
@Operation(summary = "获得业务参照配置")
@PreAuthorize("@ss.hasPermission('infra:biz-reference:query')")
public CommonResult<BizReferenceConfigRespVO> getBizReferenceConfig(@RequestParam("code") String code)
```

响应 VO：

```java
public class BizReferenceConfigRespVO {
    private String code;
    private String name;
    private String valueField;
    private String labelField;
    private String rowKey;
    private Integer pageSize;
    private List<BizReferenceFieldRespVO> columns;
    private List<BizReferenceFieldRespVO> searchSchema;
    private Map<String, String> returnFields;
}
```

字段 VO：

```java
public class BizReferenceFieldRespVO {
    private String field;
    private String title;
    private String label;
    private String component;
    private String dictType;
    private String width;
}
```

前端字段含义：

- `columns[].field/title/width` 映射到 `ApiTableSelect` 表格列。
- `searchSchema[].field/label/component/dictType` 映射到搜索 schema。
- `returnFields` 为“表单字段名 -> 参照行字段名”的映射。

### 4.2 分页查询参照数据

```http
GET /admin-api/infra/biz-reference/page?code={referenceCode}&pageNo=1&pageSize=10&nickname=张
```

方法签名建议：

```java
@GetMapping("/page")
@Operation(summary = "获得业务参照分页")
@PreAuthorize("@ss.hasPermission('infra:biz-reference:query')")
public CommonResult<PageResult<Map<String, Object>>> getBizReferencePage(@Valid BizReferencePageReqVO pageReqVO)
```

请求 VO：

```java
public class BizReferencePageReqVO extends PageParam {
    @NotBlank(message = "参照编码不能为空")
    private String code;

    private Map<String, Object> params;
}
```

如果 Spring MVC 直接绑定动态参数不方便，Service 可从 `HttpServletRequest#getParameterMap()` 提取除 `code/pageNo/pageSize` 外的搜索字段，但必须只允许已配置且启用的 `search_visible` 字段参与查询。

响应：

```java
CommonResult<PageResult<Map<String, Object>>>
```

其中 `PageResult` 的 `list` 每行只包含配置启用字段的别名，不返回未配置列。

## 5. Code Style

### 5.1 Controller

- 使用 `@Tag(name = "管理后台 - 业务参照")`。
- 使用 `@Validated`。
- 返回 `CommonResult`，通过 `success(...)` 包装。
- 权限至少要求 `infra:biz-reference:query`。
- Controller 不拼 SQL，不做复杂业务逻辑。

### 5.2 Service

Service 负责：

1. 根据 `code` 查询启用的参照配置。
2. 查询启用字段配置，并按 `sort` 排序。
3. 校验参照级业务权限 `permission`。
4. 校验所有 SQL 标识符。
5. 过滤请求参数，只保留启用搜索字段。
6. 构建查询描述对象传给 `BizReferenceQueryMapper`。
7. 组装配置响应和分页响应。

### 5.3 Mapper 与 SQL

允许使用 XML 动态 SQL，但必须满足：

- `${}` 只能用于已通过白名单校验的表名、字段名、别名。
- 所有用户输入值必须使用 `#{}` 参数绑定。
- 不允许接收前端传来的 SQL 片段。
- 不允许配置表存储可执行 SQL 片段。

建议创建内部查询对象：

```java
public class BizReferenceQueryParam {
    private String tableName;
    private List<SelectField> selectFields;
    private List<SearchCondition> conditions;
    private Integer pageNo;
    private Integer pageSize;
}
```

其中 `SelectField`、`SearchCondition` 只由 Service 根据配置生成。

### 5.4 SQL 标识符校验

所有表名、字段名、别名必须满足：

```text
[A-Za-z_][A-Za-z0-9_]*
```

如果需要支持达梦 schema 或表名前缀，首版仍不允许前端参与；是否支持 `schema.table` 需要单独确认。

### 5.5 权限、租户、逻辑删除

- `infra:biz-reference:query` 是访问通用接口的基础权限。
- 如果 `infra_biz_reference.permission` 不为空，Service 额外校验当前登录用户具备该权限。
- 如果配置 `tenant_column`，Service 自动追加当前租户条件。
- 如果配置 `deleted_column` 和 `deleted_value`，Service 自动追加逻辑删除条件。
- 首版不支持在配置表写任意数据权限 SQL。

## 6. Testing Strategy

### 6.1 单元测试

至少覆盖：

1. `code` 不存在或禁用时返回业务错误。
2. 禁用字段不会出现在 columns/searchSchema/returnFields/list。
3. 非法表名、字段名、别名被拒绝。
4. 请求中未配置的搜索字段被忽略。
5. `eq`、`like`、`in`、`between` 条件转换正确。
6. `like` 的 `%` 拼接由后端完成，值仍参数化绑定。
7. 租户字段、逻辑删除字段按配置追加。
8. 无参照级权限时拒绝访问。

### 6.2 Mapper/集成测试

如测试环境允许连接测试数据库，增加最小配置和样例数据，验证：

1. `/config` 返回列、搜索字段和回写字段元数据。
2. `/page` 返回 `list/total`。
3. 分页从 `pageNo=1` 开始。
4. 搜索字段生效。
5. 未配置字段不泄露。

### 6.3 手工验收

配合前端 demo 验证：

1. 使用 `referenceCode: 'system-user'` 打开弹窗。
2. 配置字段自动生成列和搜索项。
3. 搜索、分页、单选、多选、回写字段工作正常。
4. 无权限账号不能查询受限参照。

## 7. Boundaries

### 7.1 Always do

- 复用 `CommonResult`、`PageResult`、`PageParam`。
- 复用 infra 模块现有 DO/Mapper/Service/Controller 风格。
- 所有动态 SQL 标识符来自配置表并经过白名单校验。
- 所有用户输入值参数化绑定。
- 保持租户、权限、逻辑删除过滤可控。

### 7.2 Ask first

- 是否新增参照配置管理页面。
- 是否支持 join、视图、schema.table、排序字段配置。
- 是否需要提供初始化 SQL。
- 是否将接口放在 system 模块而不是 infra 模块。

### 7.3 Never do

- 不允许前端传表名、字段名、where SQL、order SQL 给后端执行。
- 不允许配置表存储任意 SQL 并直接执行。
- 不跳过权限、租户、逻辑删除边界。
- 不修改无关业务模块。

## 8. Acceptance Criteria

- [ ] `/infra/biz-reference/config` 可按启用 `code` 返回元数据。
- [ ] `/infra/biz-reference/page` 可按启用 `code` 返回分页数据。
- [ ] 未配置字段不会参与查询或返回。
- [ ] 非法标识符被拒绝。
- [ ] 参照级权限、租户、逻辑删除过滤生效。
- [ ] 后端相关测试通过。
- [ ] 与前端 SDD 的接口路径、字段名、响应结构一致。
