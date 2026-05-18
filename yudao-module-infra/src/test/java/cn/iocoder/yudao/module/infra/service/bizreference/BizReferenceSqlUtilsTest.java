package cn.iocoder.yudao.module.infra.service.bizreference;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BizReferenceSqlUtilsTest {

    @Test
    void validateIdentifier_acceptsSimpleIdentifiers() {
        assertTrue(BizReferenceSqlUtils.isValidIdentifier("system_users"));
        assertTrue(BizReferenceSqlUtils.isValidIdentifier("dept_id"));
        assertTrue(BizReferenceSqlUtils.isValidIdentifier("nickname1"));
    }

    @Test
    void validateIdentifier_rejectsUnsafeIdentifiers() {
        assertFalse(BizReferenceSqlUtils.isValidIdentifier(""));
        assertFalse(BizReferenceSqlUtils.isValidIdentifier("1user"));
        assertFalse(BizReferenceSqlUtils.isValidIdentifier("schema.table"));
        assertFalse(BizReferenceSqlUtils.isValidIdentifier("user;drop"));
        assertFalse(BizReferenceSqlUtils.isValidIdentifier("user name"));
        assertFalse(BizReferenceSqlUtils.isValidIdentifier("`user`"));
        assertFalse(BizReferenceSqlUtils.isValidIdentifier("user--"));
    }

    @Test
    void validateIdentifier_throwsForUnsafeIdentifiers() {
        assertThrows(IllegalArgumentException.class, () -> BizReferenceSqlUtils.validateIdentifier("schema.table"));
    }

    @Test
    void isSupportedOperator_acceptsOnlyWhitelistedOperators() {
        assertTrue(BizReferenceSqlUtils.isSupportedOperator("eq"));
        assertTrue(BizReferenceSqlUtils.isSupportedOperator("like"));
        assertTrue(BizReferenceSqlUtils.isSupportedOperator("in"));
        assertTrue(BizReferenceSqlUtils.isSupportedOperator("between"));

        assertFalse(BizReferenceSqlUtils.isSupportedOperator("ne"));
        assertFalse(BizReferenceSqlUtils.isSupportedOperator("or 1=1"));
        assertFalse(BizReferenceSqlUtils.isSupportedOperator(""));
    }

}
