package com.example.namo2.domain.moim.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MoimMemoReq {
    List<LocationInfo> locationInfos;
}