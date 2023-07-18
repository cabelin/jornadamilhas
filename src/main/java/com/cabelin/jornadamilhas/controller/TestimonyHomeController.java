package com.cabelin.jornadamilhas.controller;

import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.service.TestimonyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/depoimentos-home", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TestimonyHomeController {

  private final TestimonyService testimonyService;

  @GetMapping
  public ResponseEntity<List<TestimonyResponseDto>> getRandom() {
    return ResponseEntity.ok(testimonyService.getRandomThree());
  }

}
