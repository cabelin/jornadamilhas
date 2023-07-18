package com.cabelin.jornadamilhas.service;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestimonyService {

  TestimonyResponseDto create(TestimonyRequestDto testimonyRequestDto);

  Page<TestimonyResponseDto> getAll(Pageable pageable);

}
