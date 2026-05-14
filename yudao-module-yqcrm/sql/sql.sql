-- 菜单 SQL
INSERT INTO system_menu(
    name, permission, type, sort, parent_id,
    path, icon, component, status, component_name
)
VALUES (
    '要约报备管理', '', 2, 0, 5013,
    'offer-report', '', 'yqcrm/offerreport/index', 0, 'OfferReport'
);

-- 按钮父菜单ID
-- 说明：Oracle/达梦 使用序列获取上一个插入的 ID
DECLARE
    v_parent_id NUMBER;
BEGIN
    SELECT system_menu_seq.CURRVAL INTO v_parent_id FROM DUAL;
    -- 按钮 SQL
    INSERT INTO system_menu(
        name, permission, type, sort, parent_id,
        path, icon, component, status
    )
    VALUES (
        '要约报备查询', 'yqcrm:offer-report:query', 3, 1, v_parent_id,
        '', '', '', 0
    );
    INSERT INTO system_menu(
        name, permission, type, sort, parent_id,
        path, icon, component, status
    )
    VALUES (
        '要约报备创建', 'yqcrm:offer-report:create', 3, 2, v_parent_id,
        '', '', '', 0
    );
    INSERT INTO system_menu(
        name, permission, type, sort, parent_id,
        path, icon, component, status
    )
    VALUES (
        '要约报备更新', 'yqcrm:offer-report:update', 3, 3, v_parent_id,
        '', '', '', 0
    );
    INSERT INTO system_menu(
        name, permission, type, sort, parent_id,
        path, icon, component, status
    )
    VALUES (
        '要约报备删除', 'yqcrm:offer-report:delete', 3, 4, v_parent_id,
        '', '', '', 0
    );
    INSERT INTO system_menu(
        name, permission, type, sort, parent_id,
        path, icon, component, status
    )
    VALUES (
        '要约报备导出', 'yqcrm:offer-report:export', 3, 5, v_parent_id,
        '', '', '', 0
    );
END;
/