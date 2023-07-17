package com.cabelin.jornadamilhas.controller;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.service.TestimonyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/depoimentos", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TestimonyController {

  private final TestimonyService testimonyService;

  @PostMapping
  public ResponseEntity<TestimonyResponseDto> post(@RequestBody @Validated TestimonyRequestDto testimonyRequestDto) {
    return ResponseEntity.ok(testimonyService.create(testimonyRequestDto));
  }

}
