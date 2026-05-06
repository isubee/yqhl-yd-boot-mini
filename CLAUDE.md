# CLAUDE.md

本文件用于指导 Claude Code（claude.ai/code）在本仓库中进行代码阅读、修改、验证和协作。

## 项目概览

这是 Yudao/RuoYi-Vue-Pro 单体项目的 JDK 8 / Spring Boot 2.7 精简版。当前后端是 Maven 多模块项目，只启用了系统模块和基础设施模块；根 POM 和 server POM 中有许多更大的产品模块依赖处于注释状态，不属于当前检出的有效构建范围。

主要技术栈：

- Java 8、Spring Boot 2.7.18、Maven
- MyBatis Plus、dynamic-datasource、Druid
- Redis/Redisson、Spring Security token 认证、Quartz、Springdoc/Knife4j
- Lombok 和 MapStruct 注解处理

## 常用命令

后端构建与验证：

```bash
mvn clean package -Dmaven.test.skip=true
mvn test
mvn -pl yudao-server -am package -Dmaven.test.skip=true
mvn -pl yudao-server -am test
```

添加测试后，可按类或方法运行单个测试：

```bash
mvn -Dtest=ClassName test
mvn -Dtest=ClassName#methodName test
mvn -pl yudao-module-system -am -Dtest=ClassName test
mvn -pl yudao-module-infra -am -Dtest=ClassName test
```

本地运行后端：

```bash
mvn -pl yudao-server -am spring-boot:run -Dspring-boot.run.profiles=local
# 或打包后运行：
java -jar yudao-server/target/yudao-server.jar --spring.profiles.active=local
```

Docker 相关文件在 `script/docker/` 下；可在 `script/docker/` 目录下执行：

```bash
docker compose --env-file docker.env up -d
```

或在仓库根目录执行：

```bash
docker compose -f script/docker/docker-compose.yml --env-file script/docker/docker.env up -d
```

本地依赖 Docker 前，先核对 `script/docker/docker-compose.yml` 中的 build context 是否与当前目录结构一致，因为该文件按 `script/docker` 的相对路径引用 UI/server 构建目录。

CI 当前使用以下命令构建后端：

```bash
mvn -B package --file pom.xml -Dmaven.test.skip=true
```

当前检出中 `yudao-ui/` 下没有 `package.json`，因此不要假设前端安装或构建脚本能直接在本仓库运行；如需前端工程，需要先补齐或获取对应前端项目。

## 运行依赖与配置

- 后端入口：`yudao-server/src/main/java/cn/iocoder/yudao/server/YudaoServerApplication.java`
- 主配置：`yudao-server/src/main/resources/application.yaml`
- 默认 profile：`local`
- 本地 profile 配置：`yudao-server/src/main/resources/application-local.yaml`
- 默认本地 API 端口：`48080`
- 默认本地 MySQL 数据库：`ruoyi-vue-pro`
- 默认本地 Redis：`127.0.0.1:6379`
- 数据库初始化脚本在 `sql/` 下，MySQL 主脚本为 `sql/mysql/ruoyi-vue-pro.sql`，Quartz 表脚本为 `sql/mysql/quartz.sql`。

资源 YAML 文件中包含环境示例账号密码和第三方 API key 示例。把它们视为配置，不要把这些值当作新代码中的可复用常量。

## 后端架构

根目录 `pom.xml` 是 Maven 聚合入口和依赖管理入口。当前启用的模块有：

- `yudao-dependencies`：BOM 和依赖版本管理。
- `yudao-framework`：内部 starter 模块和共享框架代码。
- `yudao-server`：很薄的 Spring Boot 容器，依赖已启用的业务模块，并产出 `yudao-server.jar`。
- `yudao-module-system`：通用业务能力，例如用户、部门、权限、字典、租户相关集成、登录/安全支持、邮件、社交登录、验证码等。
- `yudao-module-infra`：基础设施运维与研发工具，例如配置管理、文件存储、API 日志、数据源配置、定时任务、Redis 监控、WebSocket 发送 API、代码生成器模板等。

`yudao-server` 扫描 `${yudao.info.base-package}.server` 和 `${yudao.info.base-package}.module`，因此业务模块应位于 `cn.iocoder.yudao.module.*` 下，server 代码应位于 `cn.iocoder.yudao.server` 下。

## Framework 模块结构

`yudao-framework` 拆分为多个内部 starter。每个 starter 通常将框架扩展代码放在 `core` 包，将 Spring 装配放在 `config` 包。重要 starter 包括：

- `yudao-common`
- `yudao-spring-boot-starter-web`
- `yudao-spring-boot-starter-security`
- `yudao-spring-boot-starter-mybatis`
- `yudao-spring-boot-starter-redis`
- `yudao-spring-boot-starter-job`
- `yudao-spring-boot-starter-mq`
- `yudao-spring-boot-starter-websocket`
- `yudao-spring-boot-starter-monitor`
- `yudao-spring-boot-starter-protection`
- 面向业务支撑的 tenant、data permission、IP starter。

自动配置声明位于各 starter 的 `src/main/resources/META-INF/spring/` 文件中。

## 业务模块结构

当前启用的业务模块采用分层包结构：

- `controller/admin` 和 `controller/app`：REST 接口。
- `service`：业务逻辑。
- `dal/dataobject`、`dal/mysql`、`dal/redis`：持久化层。
- `api`：跨模块服务契约。
- `convert`：MapStruct 转换器。
- `enums`、`framework`、`job`、`mq`、`util`：模块内支撑代码。

新增后端功能时，应放到对应业务模块中，不要放到 `yudao-server`；server 模块应继续保持为选择并装配模块的容器。

## 新模块二次开发约定

新增业务模块时，优先参考现有 `yudao-module-system`、`yudao-module-infra` 的结构和命名，不要把业务代码混入 framework 或 server 模块。新模块应保持 Maven 模块边界清晰：根 `pom.xml` 增加模块声明，`yudao-server/pom.xml` 只负责按需引入模块依赖。

推荐结构：

```text
yudao-module-xxx/
  pom.xml
  src/main/java/cn/iocoder/yudao/module/xxx/
    api/                  # 跨模块 API 契约与 DTO
    controller/admin/     # 管理后台接口
    controller/app/       # 用户端/移动端接口
    controller/*/vo/      # ReqVO、RespVO 等接口入出参
    service/              # 业务接口与实现
    dal/dataobject/       # DO 实体
    dal/mysql/            # MyBatis Mapper
    dal/redis/            # Redis 访问与缓存对象
    convert/              # MapStruct 转换器
    enums/                # 错误码、字典枚举、业务枚举
    job/                  # Quartz 定时任务
    mq/                   # MQ 消息、生产者、消费者
    framework/            # 仅放模块内框架集成
    util/                 # 仅放模块内工具
  src/main/resources/
    mapper/               # XML Mapper（仅复杂 SQL 需要）
```

### 代码规范

- Controller 返回 `CommonResult<T>`，成功响应使用 `success(...)`；分页接口使用 `PageResult<T>`。
- 管理后台接口放 `controller/admin`，移动端或用户端接口放 `controller/app`，不要混用路径和权限模型。
- Controller 入参使用 `@Valid` + ReqVO，接口文档使用 `@Tag`、`@Operation`、`@Parameter`/`@Schema`，权限使用 `@PreAuthorize`。
- Service 负责业务校验、事务和编排；Mapper 只放数据库访问，优先继承 `BaseMapperX` 并使用 `LambdaQueryWrapperX` 的 `xxxIfPresent` 方法。
- DO/VO/DTO 不混用；对象转换放 `convert` 包的 MapStruct 转换器，不在 Controller 手写大量字段拷贝。
- 业务异常使用模块内 `ErrorCodeConstants` + `ServiceExceptionUtil.exception(...)`，不要直接抛 `RuntimeException` 或返回字符串错误。
- 新增 SQL、菜单、权限标识、字典等初始化数据时，放到对应 `sql/` 脚本或模块约定位置，并与接口权限标识保持一致。

### 安全与权限约定

- 管理后台新增接口默认必须配置 `@PreAuthorize("@ss.hasPermission('模块:资源:动作')")`，权限标识需与菜单/按钮权限数据一致。
- 只有登录、公开文件访问、回调等明确公开接口才能使用 `@PermitAll`；新增公开接口前要确认不会泄露租户、用户、配置或文件信息。
- 数据权限默认开启；只有缓存构建、系统级任务、跨租户初始化等有明确理由的场景，才能使用 `@DataPermission(enable = false)`。
- 租户隔离默认开启；只有平台级定时任务、全局配置、初始化流程等明确跨租户场景，才能使用 `@TenantIgnore`。
- 不要在新代码中复用 YAML 示例账号、密码、API key；这些值只能作为本地配置样例。
- 文件上传、导出、回调、富文本、动态 SQL 等边界入口必须校验输入，并避免路径穿越、越权访问、SQL 注入和敏感信息回显。

### 禁止修改范围

除非任务明确要求并说明影响范围，不要修改以下内容：

- 根 `pom.xml`、`yudao-dependencies/pom.xml` 的依赖版本管理和已注释的大模块开关。
- `yudao-framework` 中通用 starter 的核心行为、自动配置和安全/租户/数据权限拦截器。
- `yudao-server` 的扫描包、启动类和全局配置结构。
- 现有 SQL 初始化脚本中的历史表结构、菜单权限、字典数据；新增内容应追加，不要重排或大面积格式化。
- `application*.yaml` 中与本地开发无关的第三方服务示例配置；不要把示例密钥提取为代码常量。
- 上游占位前端目录和 README 指向的外部工程，除非任务明确是补齐或接入前端项目。

## 前端目录

`yudao-ui/` 包含多个上游前端版本的 README 占位，以及少量 Vue3 MES 相关源码。当前检出不包含完整可运行的前端应用或 package 元数据。如果任务涉及管理后台 UI，先确认所需前端源码是否已经在本地，或是否需要从 README 指向的上游前端仓库获取。

## 测试

当前检出没有 `*Test.java` 文件。测试依赖由 `yudao-dependencies` 管理，包括 `spring-boot-starter-test`、Mockito inline、Podam、jedis-mock。添加测试后，使用 Maven Surefire 标准的 `-Dtest=...` 语法运行聚焦测试。修改业务逻辑时，优先补充聚焦的 Service/Mapper 单元测试；无法添加测试时，应至少运行相关模块的 Maven 验证命令。

## Claude Code + Codex 协作

### 分工
- Claude Code = 大脑:规划、搜索、决策、审查、协调
- Codex = 双手:代码生成、重构、修复(通过 codex MCP 工具或 codex-plugin-cc)

### Codex-First
非琐碎代码任务(>20 行成段代码 / 跨多文件)默认委托 Codex。
仅以下情况由 Claude Code 直接做:
- 笔误、注释、<20 行配置或常量调整
- 完全非代码工作(规划、解释、文档)

### 调用规约
通过 MCP 调用 codex 工具时,prompt 必须包含:
1. 任务目标(一句话)
2. 工作目录 cd(尽量精确到子目录,而非项目根)
3. sandbox 选择(read-only / workspace-write)
4. 从项目 CLAUDE.md 摘录的相关约束
5. 用 Glob/Grep 找出的「参考已有实现」具体路径
6. 期望输出格式(unified diff 或完整文件)

### 审查
Codex 产出 = 草案,不是终稿。无论通过 MCP 还是 codex-plugin-cc 命令,
Codex 返回的代码 Claude Code 必须:
- 对照项目级规则与审查清单逐条核对
- 发现违规 → 让 Codex 修,不要 Claude Code 自己改
- 通过审查后再落盘 + commit

### 反盲信
Codex 是协作者,不是上级。Claude Code 必须保留独立判断,
对可疑方案提出质疑。不当甩手掌柜。

### 项目级补充
具体项目的协作规则、调用模板、审查清单见
<项目根>/.claude/codex-collaboration.md