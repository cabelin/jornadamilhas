package com.cabelin.jornadamilhas.utils;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String serializeTestimonyRequestDto(TestimonyRequestDto dto) throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }

  public static TestimonyRequestDto deserializeTestimonyRequestDto(String json) throws JsonProcessingException {
    return objectMapper.readValue(json, TestimonyRequestDto.class);
  }

}
