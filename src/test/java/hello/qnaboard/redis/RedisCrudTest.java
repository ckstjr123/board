package hello.qnaboard.redis;

import hello.qnaboard.service.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class RedisCrudTest {

    private final String KEY = "key";
    private final String VALUE = "value";

    @Autowired
    private RedisUtil redisUtil;

    @AfterEach
    void clear() {
        this.redisUtil.deleteData(KEY);
    }

    @Test
    @DisplayName("Redis에 데이터를 저장 및 조회할 수 있음")
    void saveAndFind() {
        // given
        this.redisUtil.setData(KEY, VALUE);

        // when
        String findValue = this.redisUtil.getValue(KEY);

        // then
        log.info("findValue: {}", findValue);
        assertThat(VALUE).isEqualTo(findValue);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 수정할 수 있음")
    void update() {
        // given
        this.redisUtil.setData(KEY, VALUE);
        assertThat(this.redisUtil.getValue(KEY)).isEqualTo(VALUE);

        // when
        String updateValue = "updateValue";
        this.redisUtil.setData(KEY, updateValue);

        // then
        String findValue = this.redisUtil.getValue(KEY);
        log.info("findValue: {}", findValue);
        assertThat(findValue).isEqualTo(updateValue);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 삭제할 수 있음")
    void delete() {
        // given
        this.redisUtil.setData(KEY, VALUE);
        assertThat(this.redisUtil.getValue(KEY)).isEqualTo(VALUE);

        // when
        this.redisUtil.deleteData(KEY);
        String findValue = this.redisUtil.getValue(KEY);

        // then
        log.info("deletedValue: {}", findValue);
        assertThat(findValue).isNull();
    }

    @Test
    @DisplayName("Redis에 해당 key로 저장된 데이터가 있는지 여부를 확인할 수 있음")
    void isExists() {
        // given
        this.redisUtil.setData(KEY, VALUE);
        log.info("data is exists: " + this.redisUtil.getValue(KEY));

        // then
        assertThat(this.redisUtil.isExists(KEY)).isTrue();

        // and then given
        this.redisUtil.deleteData(KEY);
        log.info("data is deleted: " + this.redisUtil.getValue(KEY));

        // then
        assertThat(this.redisUtil.isExists(KEY)).isFalse();
    }

    @Test
    @DisplayName("Redis에 유효기간이 지정된 데이터는 만료 시 삭제됨")
    void expire() throws InterruptedException {
        // given
        this.redisUtil.setDataExpire(KEY, VALUE, 30); // 데이터 유효시간 30초

        // when
        String findValue = this.redisUtil.getValue(KEY);
        log.info("findValue: {}", findValue);
        Thread.sleep(30000);

        // then
        String expiredValue = this.redisUtil.getValue(KEY);
        log.info("expiredValue: {}", expiredValue);
        assertThat(expiredValue).isNull();
    }

}