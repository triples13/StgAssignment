package com.stglabs.interview.service;

import com.stglabs.interview.dto.CharacterFrequencyEndpointResponse;
import com.stglabs.interview.dto.CharacterFrequencyResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@Log4j2
public class FrequencyCountService {

    @Value("${endpoint.external.frequencycount}")
    private String frequencyCountEndpoint;


    @Autowired
    private RestTemplate restTemplate;


    @CircuitBreaker(name =  "externalPostEndpoint", fallbackMethod = "defaultFallBack")
    @Retry(name= "frequencyEndpointRetry")
    public List<CharacterFrequencyResponse> countCharactersByFrequency(){
      log.info("invoking endpoint {}", frequencyCountEndpoint);
        var responseEntity = restTemplate.exchange(frequencyCountEndpoint, HttpMethod.GET,null, new ParameterizedTypeReference<List<CharacterFrequencyEndpointResponse>>() {
        });
        var responseBody = responseEntity.getBody();

       return responseBody.stream().
                map(resp -> CharacterFrequencyResponse.builder().postId(resp.getPostId())
                        .characterFrequencyMap(getFrequencyMap(resp.getBody())).build()).collect(Collectors.toList());
    }

    public void defaultFallBack(final HttpClientErrorException e){
       log.error("bad request can be sent to dlq or other process for inspection", e.getMessage());
    }

    public List<CharacterFrequencyResponse> defaultFallBack(Exception e){
        e.printStackTrace();
        log.error("failed to invoke frequency endpoint due to {} , falling back to default ", e.getMessage());
        return new ArrayList<>();
    }


    private Map<String, Long> getFrequencyMap(String body) {
       return  body.codePoints().mapToObj(Character::toString).
                filter(Predicate.not(ch->ch.equals("\n"))).
               collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
    }
}
