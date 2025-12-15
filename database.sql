/*
 Navicat Premium Data Transfer

 Source Server         : MyDatabase
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : localhost:3306
 Source Schema         : database

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 05/05/2025 13:17:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `province` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `city` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `address` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `zip` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '2003-02-14', '张三', '安徽', '合肥', '安徽理工大学', '');
INSERT INTO `user` VALUES (2, '2004-02-25', '李四', '湖北', '武汉', '武汉理工大学', NULL);
INSERT INTO `user` VALUES (3, '2002-03-19', '周永', '四川', '成都', '四川大学', NULL);
INSERT INTO `user` VALUES (4, '2005-04-20', '吴涛', '广东', '广州', '中山大学', NULL);
INSERT INTO `user` VALUES (5, '2002-11-23', '李诗', '山东', '济南', '山东大学', NULL);
INSERT INTO `user` VALUES (6, '2001-12-03', '郭雨', '浙江', '杭州', '浙江大学', NULL);
INSERT INTO `user` VALUES (7, '2003-06-07', '张豪', '陕西', '西安', '西安交通大学', NULL);
INSERT INTO `user` VALUES (8, '2004-08-09', '唐石', '上海', '上海', '复旦大学', NULL);
INSERT INTO `user` VALUES (9, '2003-04-15', '陈源', '湖北', '武汉', '武汉大学', NULL);
INSERT INTO `user` VALUES (10, '2003-07-19', '韩天', '天津', '天津', '南开大学', NULL);
INSERT INTO `user` VALUES (14, '2004-04-04', '徐静', '湖南', '长沙', '国防科技大学', NULL);
INSERT INTO `user` VALUES (15, '2001-02-01', '陈浩', '湖北', '武汉', '武汉理工大学', NULL);

-- ----------------------------
-- Table structure for usert
-- ----------------------------
DROP TABLE IF EXISTS `usert`;
CREATE TABLE `usert`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `money` float(255, 0) NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usert
-- ----------------------------
INSERT INTO `usert` VALUES (10, '66', '6', NULL, NULL, NULL, 'http://localhost:9090/files/1715320441628-1697438073596-avatar.png');
INSERT INTO `usert` VALUES (11, '3', '3', '', NULL, NULL, 'http://localhost:9090/files/1715321505360-1698155543709-柴犬.jpeg');
INSERT INTO `usert` VALUES (12, 'taylor', '123', NULL, NULL, NULL, 'http://localhost:9090/files/1715500486270-1697438073596-avatar.png');
INSERT INTO `usert` VALUES (13, '4', '3', NULL, NULL, NULL, 'http://localhost:9090/files/1716446886778-1698155465247-柴犬.jpeg');
INSERT INTO `usert` VALUES (14, '5', '5', NULL, NULL, NULL, 'http://localhost:9090/files/1716447113904-1697438073596-avatar.png');
INSERT INTO `usert` VALUES (15, '6', '3', '3', '2024-05-22T16:00:00.000Z', 6, 'http://localhost:9090/files/1716447566725-1697438073596-avatar.png');
INSERT INTO `usert` VALUES (16, '9', '6', '6', '2024-05-23T16:00:00.000Z', NULL, 'http://localhost:9090/files/1716530345336-1697438073596-avatar.png');
INSERT INTO `usert` VALUES (17, '10', '6', '6', '2024-05-08T16:00:00.000Z', NULL, 'http://localhost:9090/files/1716530817248-1697438073596-avatar.png');
INSERT INTO `usert` VALUES (18, '11', '6', '6', '2024-05-23T16:00:00.000Z', NULL, 'http://localhost:9090/files/1716533372543-1698155543709-柴犬.jpeg');

SET FOREIGN_KEY_CHECKS = 1;
