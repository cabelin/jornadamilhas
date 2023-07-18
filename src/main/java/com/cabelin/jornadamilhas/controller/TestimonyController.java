package com.cabelin.jornadamilhas.controller;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.service.TestimonyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/depoimentos", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TestimonyController {

  private final TestimonyService testimonyService;

  @PostMapping
  public ResponseEntity<TestimonyResponseDto> post(@RequestBody @Validated TestimonyRequestDto testimonyRequestDto) {
    return ResponseEntity.ok(testimonyService.create(testimonyRequestDto));
  }

  @GetMapping
  public ResponseEntity<Page<TestimonyResponseDto>> getAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(testimonyService.getAll(pageable));
  }

}
