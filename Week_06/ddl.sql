/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : java00

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 25/11/2020 23:53:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goods_id` int(10) NOT NULL COMMENT '商品ID',
  `goods_code` char(16) NOT NULL COMMENT '商品编码',
  `goods_name` varchar(20) NOT NULL COMMENT '商品名称',
  `goods_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `goods_desc` varchar(500) NOT NULL COMMENT '商品描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `order_status` tinyint(2) NOT NULL COMMENT '订单状态（1-准备下单，2-下单中，3-下单完成，4-取消订单，5-关闭订单）',
  `order_price` decimal(10,2) NOT NULL COMMENT '订单总价',
  `order_real_price` decimal(10,2) NOT NULL COMMENT '订单实际总价',
  `pay_time` datetime NOT NULL COMMENT '交易时间',
  `pay_price` decimal(10,2) NOT NULL COMMENT '交易总价',
  `pay_type` tinyint(2) NOT NULL COMMENT '交易方式（1-支付宝，2-微信，3-银行卡）',
  `pay_status` tinyint(2) NOT NULL COMMENT '交易状态（1-等待交易，2-交易完成，3-交易取消，4-交易超时，5-交易关闭）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `nick_name` varchar(20) NOT NULL COMMENT '用户昵称',
  `mobile_phone` int(11) NOT NULL COMMENT '手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;
