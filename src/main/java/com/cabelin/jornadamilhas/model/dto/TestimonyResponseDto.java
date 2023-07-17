package com.cabelin.jornadamilhas.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestimonyResponseDto {

  private Long id;
  private String photoUrl;
  private String text;
  private String ownerName;

}
