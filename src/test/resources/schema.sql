drop table if exists member CASCADE;
drop table if exists board CASCADE;
drop table if exists board_recommend CASCADE;
drop table if exists comment CASCADE;

CREATE TABLE `member` (
`member_id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
`name` varchar(15) NOT NULL,
`email` varchar(255) UNIQUE NOT NULL,
`password` varchar(16) NOT NULL,
`role` varchar(255),
`reg_time` timestamp NOT NULL,
`modified_by` varchar(15),
`update_time` timestamp
);

CREATE TABLE `board` (
`board_id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
`member_id` bigint NOT NULL,
`title` varchar(40) NOT NULL,
`view` bigint NOT NULL,
`board_content` text NOT NULL,
`board_category` varchar(255) NOT NULL,
`reg_time` timestamp NOT NULL,
`modified_by` varchar(15),
`update_time` timestamp
);

CREATE TABLE `board_recommend` (
`member_id` bigint NOT NULL,
`board_id` bigint NOT NULL,
PRIMARY KEY (`member_id`, `board_id`)
);

CREATE TABLE `comment` (
`comment_id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
`board_id` bigint NOT NULL,
`member_id` bigint NOT NULL,
`reg_time` timestamp NOT NULL,
`comment_content` varchar(10000) NOT NULL,
`modified_by` varchar(15),
`update_time` timestamp
);

ALTER TABLE `board` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`);

ALTER TABLE `board_recommend` ADD FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`);

ALTER TABLE `board_recommend` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);