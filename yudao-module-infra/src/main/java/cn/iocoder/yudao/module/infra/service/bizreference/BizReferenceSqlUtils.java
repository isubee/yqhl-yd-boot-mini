package cn.iocoder.yudao.module.infra.service.bizreference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class BizReferenceSqlUtils {

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList("eq", "like", "in", "between"));

    private BizReferenceSqlUtils() {
    }

    public static boolean isValidIdentifier(String identifier) {
        return identifier != null && IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    public static String validateIdentifier(String identifier) {
        if (!isValidIdentifier(identifier)) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
        return identifier;
    }

    public static boolean isSupportedOperator(String operator) {
        return operator != null && OPERATORS.contains(operator);
    }

}
