/*
 Navicat Premium Data Transfer

 Source Server         : 120.46.213.254
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : 120.46.213.254:3306
 Source Schema         : casbin

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : 65001

 Date: 01/03/2023 20:42:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for casbin_rule
-- ----------------------------
DROP TABLE IF EXISTS `casbin_rule`;
CREATE TABLE `casbin_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ptype` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `v0` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `v1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `v2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `v3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `v4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `v5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of casbin_rule
-- ----------------------------
INSERT INTO `casbin_rule` VALUES (55, 'p', 'user_1', '/ask', 'POST', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (56, 'p', 'user_4', '/ask', 'POST', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (57, 'p', 'user_3', '/ask', 'POST', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (58, 'p', 'user_6', '/ask', 'POST', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (59, 'p', 'user_2', '/test', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (60, 'p', 'user_7', '/test', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (61, 'p', 'user_1', '/go', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (62, 'p', 'user_4', '/go', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (63, 'p', 'user_3', '/go', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (64, 'p', 'user_6', '/go', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (65, 'p', 'user_1', '/user/allocate/role/*', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (66, 'p', 'user_4', '/user/allocate/role/*', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (67, 'p', 'user_3', '/user/allocate/role/*', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (68, 'p', 'user_6', '/user/allocate/role/*', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (69, 'p', 'user_7', '/love', 'POST||GET||PUT', NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (70, 'g', 'user_1', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (71, 'g', 'user_2', 'role_2', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (72, 'g', 'user_4', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (73, 'g', 'user_3', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (74, 'g', 'user_6', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (75, 'g', 'user_7', 'role_2', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (76, 'g', 'user_7', 'role_3', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (77, 'g2', '/ask', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (78, 'g2', '/test', 'role_2', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (79, 'g2', '/go', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (80, 'g2', '/user/allocate/role/*', 'role_1', NULL, NULL, NULL, NULL);
INSERT INTO `casbin_rule` VALUES (81, 'g2', '/love', 'role_3', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for rel_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `rel_role_resource`;
CREATE TABLE `rel_role_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL,
  `resource_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_role_resource
-- ----------------------------
INSERT INTO `rel_role_resource` VALUES (1, 1, 1, '2021-11-20 15:08:31', '2021-11-20 15:08:34');
INSERT INTO `rel_role_resource` VALUES (2, 2, 2, '2021-11-20 15:08:40', '2021-11-20 15:08:43');
INSERT INTO `rel_role_resource` VALUES (3, 1, 3, '2021-11-20 20:55:28', '2021-11-20 20:55:30');
INSERT INTO `rel_role_resource` VALUES (4, 1, 4, NULL, NULL);
INSERT INTO `rel_role_resource` VALUES (5, 3, 6, '2021-11-21 00:13:35', '2021-11-21 00:13:35');

-- ----------------------------
-- Table structure for rel_user_role
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_role`;
CREATE TABLE `rel_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_user_role
-- ----------------------------
INSERT INTO `rel_user_role` VALUES (1, 1, 1, '2021-11-20 14:09:16', '2021-11-20 14:09:18');
INSERT INTO `rel_user_role` VALUES (2, 2, 2, '2021-11-20 15:03:00', '2021-11-20 15:03:04');
INSERT INTO `rel_user_role` VALUES (3, 4, 1, '2021-11-20 23:00:04', '2021-11-20 23:00:04');
INSERT INTO `rel_user_role` VALUES (6, 3, 1, '2021-11-20 23:07:55', '2021-11-20 23:07:55');
INSERT INTO `rel_user_role` VALUES (8, 6, 1, '2021-11-20 23:55:59', '2021-11-20 23:55:59');
INSERT INTO `rel_user_role` VALUES (9, 7, 2, '2021-11-21 00:02:51', '2021-11-21 00:02:51');
INSERT INTO `rel_user_role` VALUES (10, 7, 3, '2021-11-21 00:14:20', '2021-11-21 00:14:20');

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `resource_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT 0,
  `status` tinyint(4) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES (1, '提问', '/ask', 0, 1, '2021-11-20 14:09:04', '2021-11-20 21:22:33', 'POST');
INSERT INTO `sys_resource` VALUES (2, '测试', '/test', 0, 1, '2021-11-20 20:48:31', '2021-11-20 21:15:58', 'POST||GET||PUT');
INSERT INTO `sys_resource` VALUES (3, '去', '/go', 0, 1, '2021-11-20 20:55:08', '2021-11-20 21:15:53', 'POST||GET||PUT');
INSERT INTO `sys_resource` VALUES (4, '为用户分配角色', '/user/allocate/role/*', 0, 1, '2021-11-20 22:58:24', '2021-11-20 22:58:35', 'POST||GET||PUT');
INSERT INTO `sys_resource` VALUES (5, '为角色添加资源权限', '/user/allocate/role/resource', 0, 1, NULL, '2021-11-21 00:07:21', 'POST||GET||PUT');
INSERT INTO `sys_resource` VALUES (6, '爱', '/love', 0, 1, NULL, '2021-11-21 00:09:21', 'POST||GET||PUT');
INSERT INTO `sys_resource` VALUES (7, '生活', '/live', 0, 1, NULL, NULL, 'POST||GET||PUT');
INSERT INTO `sys_resource` VALUES (8, '笑', '/laugh', 0, 1, NULL, NULL, 'POST||GET||PUT');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '管理员', '1', '2021-11-20 14:08:35', '2021-11-20 14:08:37');
INSERT INTO `sys_role` VALUES (2, 'test', '测试用户', '1', '2021-11-20 15:02:10', '2021-11-20 15:02:12');
INSERT INTO `sys_role` VALUES (3, 'opt', '运营', '1', '2021-11-21 00:08:16', '2021-11-21 00:08:18');

SET FOREIGN_KEY_CHECKS = 1;
