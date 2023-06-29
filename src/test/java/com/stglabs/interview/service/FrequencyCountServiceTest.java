package com.stglabs.interview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stglabs.interview.config.TestConfig;
import com.stglabs.interview.dto.CharacterFrequencyEndpointResponse;
import com.stglabs.interview.service.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = FrequencyCountService.class)
@Import(TestConfig.class)
public class FrequencyCountServiceTest {

    @Autowired
    FrequencyCountService frequencyCountService;
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void testCountByCharacterSingleDataResponseIsSuccess() throws IOException {
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<CharacterFrequencyEndpointResponse>>>any())).thenReturn(getResponseEntity());
        var response = frequencyCountService.countCharactersByFrequency();
        Assertions.assertTrue(response.size() == 1);
        Assertions.assertTrue(response.get(0).getPostId() == 1);
        Assertions.assertTrue(response.get(0).getCharacterFrequencyMap().size() == 4);
    }

    private ResponseEntity<List<CharacterFrequencyEndpointResponse>> getResponseEntity() throws IOException {
       var jsonData = Arrays.asList(objectMapper.readValue(TestUtil.readFile("TestData.json"),
                CharacterFrequencyEndpointResponse[].class));
      return ResponseEntity.of(Optional.of(jsonData));
    }
}
