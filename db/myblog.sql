/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : myblog

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 30/10/2020 20:53:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题\r\n',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `ctime` int(13) NULL DEFAULT NULL COMMENT '创建时间\r\n\r\n',
  `views` int(11) NULL DEFAULT 0 COMMENT '观看次',
  `thumbs` int(11) NULL DEFAULT 0 COMMENT '点赞\r\n',
  `is_top` int(255) NULL DEFAULT 0 COMMENT '是否置顶',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `indx_view`(`views`) USING BTREE,
  INDEX `indx_thumbs`(`thumbs`) USING BTREE,
  INDEX `indx_is_tops`(`is_top`) USING BTREE,
  INDEX `indx_ctime`(`ctime`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (1, 'test', 'panda', 'hello', 1567605361, 0, 0, 0);

-- ----------------------------
-- Table structure for blog_tags_mapping
-- ----------------------------
DROP TABLE IF EXISTS `blog_tags_mapping`;
CREATE TABLE `blog_tags_mapping`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) NULL DEFAULT NULL COMMENT '博客id',
  `tags_id` int(11) NULL DEFAULT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UQ_BLOG_ID_TAGS_ID`(`blog_id`, `tags_id`) USING BTREE COMMENT '一个博客只能对应一个标签，不能重复',
  INDEX `idx_tags_id`(`tags_id`) USING BTREE COMMENT '根据标签查有哪些文章',
  INDEX `idx_blog_id`(`blog_id`) USING BTREE COMMENT '根据文章查对应哪些标签'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(15) NULL DEFAULT NULL COMMENT '博客id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言人名',
  `mail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言人邮箱',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言的内容',
  `ctime` int(13) NULL DEFAULT NULL COMMENT '留言的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `ctime` int(15) NULL DEFAULT NULL COMMENT '创建时间\r\n',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uq_tags`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
