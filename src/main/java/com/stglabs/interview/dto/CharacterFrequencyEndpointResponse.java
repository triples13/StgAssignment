package com.stglabs.interview.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CharacterFrequencyEndpointResponse {
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;
}
