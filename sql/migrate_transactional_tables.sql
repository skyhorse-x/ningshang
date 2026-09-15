-- 在导入 data.sql 后执行，使权限更新、删除关联等多步操作支持事务回滚。
USE ningshang;
ALTER TABLE admin ENGINE=InnoDB;
ALTER TABLE admin_group ENGINE=InnoDB;
ALTER TABLE admin_group_menu ENGINE=InnoDB;
ALTER TABLE admin_menu ENGINE=InnoDB;
