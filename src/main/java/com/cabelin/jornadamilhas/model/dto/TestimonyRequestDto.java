package com.cabelin.jornadamilhas.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestimonyRequestDto {

  @NotBlank
  private String photoUrl;

  @NotBlank
  private String text;

  @NotBlank
  private String ownerName;

}
