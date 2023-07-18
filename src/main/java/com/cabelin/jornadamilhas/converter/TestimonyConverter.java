package com.cabelin.jornadamilhas.converter;

import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.model.entity.TestimonyEntity;

public class TestimonyConverter {

  public static TestimonyResponseDto entityToResponse(TestimonyEntity entity) {
    return TestimonyResponseDto.builder()
        .id(entity.getId())
        .photoUrl(entity.getPhotoUrl())
        .text(entity.getText())
        .ownerName(entity.getOwnerName())
        .build();
  }

}
