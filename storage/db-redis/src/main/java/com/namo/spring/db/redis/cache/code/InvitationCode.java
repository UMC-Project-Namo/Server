package com.namo.spring.db.redis.cache.code;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("invitationCode")
@ToString(of = {"code", "scheduleId"})
@EqualsAndHashCode(of = {"code", "scheduleId"})
public class InvitationCode {
    @Id
    private final String code;
    private final Long scheduleId;

    private final long ttl;

    @Builder
    private InvitationCode(String code, Long scheduleId, long ttl) {
        this.code = code;
        this.scheduleId = scheduleId;
        this.ttl = ttl;
    }

    public static InvitationCode of(String code, Long scheduleId, long ttl) {
        return InvitationCode.builder()
                .code(code)
                .scheduleId(scheduleId)
                .ttl(ttl)
                .build();
    }
}
