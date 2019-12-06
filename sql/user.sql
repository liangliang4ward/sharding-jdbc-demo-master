drop table if exists user;
CREATE TABLE `user` (
  `id` bigint(20) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `age` int(20) DEFAULT NULL,
  `town_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

