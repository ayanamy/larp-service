CREATE DATABASE `LARP` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
CREATE TABLE `clues` (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_id` int DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL COMMENT '线索编号',
  `location` varchar(45) DEFAULT NULL COMMENT '线索来源',
  `description` varchar(45) DEFAULT NULL COMMENT '线索的描述',
  `images` varchar(500) DEFAULT NULL COMMENT '线索的图片',
  `status` int DEFAULT NULL COMMENT '0 未获取的线索 1已获取的线索 2已公开的线索',
  `role_id` int DEFAULT NULL COMMENT '这个线索给了哪个角色',
  `round` int DEFAULT NULL COMMENT '第几轮线索',
  `pick_time` datetime DEFAULT NULL COMMENT '获取线索的时间',
  `clue_type` varchar(45) DEFAULT NULL COMMENT '线索类型 ''normal''普通线索,''special''特殊线索',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=708 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_name` varchar(45) DEFAULT NULL COMMENT '游戏名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '开始时间',
  `status` int DEFAULT NULL COMMENT '状态 -1为已玩过的 0为还未玩过 1为当前在玩的',
  `max_user` int DEFAULT NULL COMMENT '最大人数',
  `min_user` int DEFAULT NULL COMMENT '最小人数',
  `clues_enable` int DEFAULT NULL COMMENT '当前是否开启了线索 1开启 0未开启',
  `max_clues` int DEFAULT NULL COMMENT '每轮最多获取线索数量',
  `round` int DEFAULT NULL COMMENT '当前是第几幕',
  `dm` varchar(45) DEFAULT NULL COMMENT '主持人',
  `round_total` int DEFAULT NULL COMMENT '总共有几轮',
  `handbook` varchar(500) DEFAULT NULL COMMENT '组织者手册',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_id` int DEFAULT NULL COMMENT '游戏id',
  `role_name` varchar(45) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
  `user` varchar(45) DEFAULT NULL COMMENT '玩家',
  `status` int DEFAULT NULL COMMENT '状态',
  `images` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `scripts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `round` int DEFAULT NULL COMMENT '第几幕的剧本',
  `content` varchar(500) DEFAULT NULL COMMENT '剧本内容',
  `order_no` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=693 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `role` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


