/*
 wtp - 1.0.0
 
 Date: 16/09/2020 16:03:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'AppID',
  `app_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '应用名',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `created` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `modified` bigint(20) NOT NULL DEFAULT 0 COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for cluster
-- ----------------------------
DROP TABLE IF EXISTS `cluster`;
CREATE TABLE `cluster`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'AppId',
  `cluster_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '集群名字',
  `cluster_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'cluster Id',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  `created` bigint(20) NOT NULL DEFAULT 0,
  `modified` bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `role` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'user',
  `status` tinyint(255) NOT NULL DEFAULT 10,
  `created` bigint(255) NOT NULL DEFAULT 0,
  `modified` bigint(255) NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

INSERT INTO `wtp`.`user`(`nickname`, `avatar`, `username`, `password`, `phone`, `email`, `role`, `status`, `created`, `modified`) VALUES ('Supper Admin', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', 'wtpwtp', '5e843ec165eee460b42f6619a92286fd', '', '', 'SUPPER-ADMIN', 10, 1598016851125, 1598016851125);

-- ----------------------------
-- Table structure for user_app_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_app_permission`;
CREATE TABLE `user_app_permission`  (
  `user_app_permission_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT 0,
  `app_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `created` bigint(20) NOT NULL DEFAULT 0,
  `modified` bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_app_permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for wtp
-- ----------------------------
DROP TABLE IF EXISTS `wtp`;
CREATE TABLE `wtp`  (
  `wtp_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `cluster_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `title` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `owner_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `alarm_email` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `open_alarm` bit(1) NOT NULL DEFAULT b'0',
  `pool_alarm_threshold` int(11) NOT NULL DEFAULT 90,
  `queue_alarm_threshold` int(11) NOT NULL DEFAULT 80,
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `core_pool_size` int(11) NOT NULL DEFAULT 0,
  `maximum_pool_size` int(11) NOT NULL DEFAULT 0,
  `keep_alive_seconds` bigint(20) NOT NULL DEFAULT 0,
  `queue_capacity` int(11) NOT NULL DEFAULT 0,
  `queue_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `rejected_execution_handler_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  `created` bigint(20) NOT NULL DEFAULT 0,
  `modified` bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`wtp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for wtp_log
-- ----------------------------
DROP TABLE IF EXISTS `wtp_log`;
CREATE TABLE `wtp_log`  (
  `wtp_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `cluster_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `core_pool_size` int(11) NOT NULL DEFAULT 0,
  `maximum_pool_size` int(11) NOT NULL DEFAULT 0,
  `keep_alive_seconds` bigint(20) NOT NULL DEFAULT 0,
  `active_count` int(11) NOT NULL DEFAULT 0,
  `completed_task_count` bigint(20) NOT NULL DEFAULT 0,
  `queue_size` int(11) NOT NULL DEFAULT 0,
  `queue_remaining_capacity` int(11) NOT NULL DEFAULT 0,
  `largest_pool_size` int(11) NOT NULL DEFAULT 0,
  `rejected_execution_count` int(11) NOT NULL DEFAULT 0,
  `pool_size` int(11) NOT NULL DEFAULT 0,
  `task_count` bigint(20) NOT NULL DEFAULT 0,
  `total_time` bigint(20) NOT NULL DEFAULT 0,
  `maximum_time` bigint(20) NOT NULL DEFAULT 0,
  `log_time` bigint(20) NOT NULL DEFAULT 0,
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  `created` bigint(20) NOT NULL DEFAULT 0,
  `modified` bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`wtp_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for wtp_registry
-- ----------------------------
DROP TABLE IF EXISTS `wtp_registry`;
CREATE TABLE `wtp_registry`  (
  `wtp_registry_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `cluster_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `last_pull_time` bigint(20) NOT NULL DEFAULT 0,
  `created` bigint(20) NOT NULL DEFAULT 0,
  `modified` bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`wtp_registry_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- init data
-- ----------------------------
INSERT INTO `wtp`.`user`(`user_id`, `nickname`, `avatar`, `username`, `password`, `phone`, `email`, `role`, `status`, `created`, `modified`) VALUES (1, 'Supper Admin', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', 'wtpwtp', '5e843ec165eee460b42f6619a92286fd', '', '', 'SUPPER-ADMIN', 10, 0, 0);
INSERT INTO `wtp`.`user`(`user_id`, `nickname`, `avatar`, `username`, `password`, `phone`, `email`, `role`, `status`, `created`, `modified`) VALUES (2, 'user', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', 'user', '5e843ec165eee460b42f6619a92286fd', '', '11', 'USER', 10, 0, 1600957552652);
INSERT INTO `wtp`.`user`(`user_id`, `nickname`, `avatar`, `username`, `password`, `phone`, `email`, `role`, `status`, `created`, `modified`) VALUES (3, 'admin', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', 'admin', '5e843ec165eee460b42f6619a92286fd', '', '', 'ADMIN', 10, 1598016851125, 1598016851125);


