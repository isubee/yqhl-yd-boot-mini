package cn.iocoder.yudao.module.yqcrm.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * YQCRM 错误码枚举类
 *
 * yqcrm 系统，使用 1-010-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 要约报备 1-010-000-000 ==========
    ErrorCode OFFER_REPORT_NOT_EXISTS = new ErrorCode(1_010_000_000, "要约报备不存在");

}
