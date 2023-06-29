package com.stglabs.interview.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
@Builder
@Getter
public class CharacterFrequencyResponse {
    private int postId;
    private Map<String, Long> characterFrequencyMap;
}
