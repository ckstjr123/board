drop table if exists member CASCADE;
drop table if exists board CASCADE;
drop table if exists board_recommend CASCADE;
drop table if exists comment CASCADE;

CREATE TABLE `member` (
`member_id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
`name` varchar(18) NOT NULL,
`email` varchar(255) UNIQUE NOT NULL,
`password` varchar(60),
`role` varchar(255),
`reg_time` timestamp(9) NOT NULL, -- 년-월-일 시:분:초:fractional seconds(LocalDateTime의 fractional seconds는 기본 9자리, 테스트용 DB인 H2의 timestamp fractional seconds는 기본 6자리이므로 timestamp(9)로 지정)
`update_time` timestamp(9)
);

CREATE TABLE `board` (
`board_id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
`member_id` bigint NOT NULL,
`writer_name` varchar(18) NOT NULL,
`title` varchar(40) NOT NULL,
`view` bigint NOT NULL DEFAULT 0,
`content` varchar(10000) NOT NULL,
`board_type` varchar(255) NOT NULL,
`reg_time` timestamp(9) NOT NULL,
`update_time` timestamp(9)
);

CREATE TABLE `board_recommend` (
`member_id` bigint NOT NULL,
`board_id` bigint NOT NULL,
`vote` tinyint NOT NULL,
PRIMARY KEY (`member_id`, `board_id`)
);

CREATE TABLE `comment` (
`comment_id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
`board_id` bigint NOT NULL,
`member_id` bigint NOT NULL,
`writer_name` varchar(18) NOT NULL,
`reg_time` timestamp(9) NOT NULL,
`comment_content` varchar(10000) NOT NULL,
`update_time` timestamp(9)
);

--ALTER TABLE `board` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);
--ALTER TABLE `comment` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`);

ALTER TABLE `board_recommend` ADD FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`);

ALTER TABLE `board_recommend` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

-- 반정규화(게시물 추천 수, 게시물 댓글 수)
ALTER TABLE board ADD COLUMN board_recommend_count bigint NOT NULL DEFAULT 0;
ALTER TABLE board ADD COLUMN board_comment_count bigint NOT NULL DEFAULT 0;