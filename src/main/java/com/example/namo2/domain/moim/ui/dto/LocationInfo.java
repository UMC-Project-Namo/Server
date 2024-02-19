package com.example.namo2.domain.moim.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class LocationInfo {
    private String name;
    private Integer money;
    private List<Long> participants;

    public LocationInfo(String name, String money, String participants) {
        this.name = name;
        this.money = Integer.valueOf(money);
        this.participants = Arrays.stream(participants.replace(" ", "").split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
