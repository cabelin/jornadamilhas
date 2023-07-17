package com.cabelin.jornadamilhas.controller;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.service.TestimonyService;
import com.cabelin.jornadamilhas.utils.FileUtil;
import com.cabelin.jornadamilhas.utils.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TestimonyControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TestimonyService testimonyService;

  @Test
  public void createTestimony_whenPostTestimony_thenStatus200() throws Exception {
    String testimonyJson = FileUtil.readFromFileToString("/files/testimony.json");
    TestimonyRequestDto testimonyRequestDto = MapperUtil.deserializeTestimonyRequestDto(testimonyJson);

    given(testimonyService.create(any(TestimonyRequestDto.class)))
        .willReturn(TestimonyResponseDto.builder()
            .id(29L)
            .photoUrl(testimonyRequestDto.getPhotoUrl())
            .text(testimonyRequestDto.getText())
            .ownerName(testimonyRequestDto.getOwnerName())
            .build());

    mvc.perform(post("/depoimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testimonyJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(29)))
        .andExpect(jsonPath("photoUrl", is("http://localhost:8080/img")))
        .andExpect(jsonPath("text", is("depoimento fictício")))
        .andExpect(jsonPath("ownerName", is("José")));
  }

  @Test
  public void createTestimony_whenPostTestimonyNoBody_thenStatus400() throws Exception {
    mvc.perform(post("/depoimentos")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @ParameterizedTest
  @CsvSource({",anyText,anyOwnerName", "anyPhotoUrl,,anyOwnerName", "anyPhotoUrl,anyText,", ",,"})
  public void createTestimony_whenPostTestimonyInvalidBody_thenStatus400(
      String photoUrl, String text, String ownerName
  ) throws Exception {
    TestimonyRequestDto dto = TestimonyRequestDto.builder()
        .photoUrl(photoUrl)
        .text(text)
        .ownerName(ownerName)
        .build();

    mvc.perform(post("/depoimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(MapperUtil.serializeTestimonyRequestDto(dto)))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

}
