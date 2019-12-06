drop table if exists order_info_0;
CREATE TABLE `order_info_0` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `order_no` varchar(100) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


drop table if exists order_info_1;
CREATE TABLE `order_info_1` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `order_no` varchar(100) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
