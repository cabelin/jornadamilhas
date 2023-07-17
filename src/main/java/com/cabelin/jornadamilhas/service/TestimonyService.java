package com.cabelin.jornadamilhas.service;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;

public interface TestimonyService {

  TestimonyResponseDto create(TestimonyRequestDto testimonyRequestDto);

}
