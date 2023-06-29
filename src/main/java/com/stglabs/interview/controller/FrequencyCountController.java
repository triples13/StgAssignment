package com.stglabs.interview.controller;

import com.stglabs.interview.dto.CharacterFrequencyResponse;
import com.stglabs.interview.service.FrequencyCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/frequency/count")
public class FrequencyCountController {

    @Autowired
    FrequencyCountService frequencyCountService;

    @GetMapping
    public ResponseEntity<List<CharacterFrequencyResponse>> getCharacterCountByFrequency(){
        var response  = frequencyCountService.countCharactersByFrequency();
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
