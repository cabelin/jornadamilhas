package com.cabelin.jornadamilhas.controller;

import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.service.TestimonyService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TestimonyHomeControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TestimonyService testimonyService;

  public static class Paths {
    private static final String BASE_PATH = "/depoimentos-home";

    public static String getUrl() {
      return BASE_PATH;
    }

  }

  @Test
  public void getAllTestimony_whenHasTestimonies_thenStatus200WithElements() throws Exception {
    List<TestimonyResponseDto> testimonyResponseDtos = Arrays.asList(
        TestimonyResponseDto.builder()
            .photoUrl("Photo url 001")
            .ownerName("Owner 001")
            .text("Text 001")
            .build(),
        TestimonyResponseDto.builder()
            .photoUrl("Photo url 002")
            .ownerName("Owner 002")
            .text("Text 002")
            .build(),
        TestimonyResponseDto.builder()
            .photoUrl("Photo url 003")
            .ownerName("Owner 003")
            .text("Text 003")
            .build()
    );

    when(testimonyService.getRandomThree()).thenReturn(testimonyResponseDtos);

    mvc.perform(get(Paths.getUrl())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].photoUrl", is("Photo url 001")))
        .andExpect(jsonPath("[0].ownerName", is("Owner 001")))
        .andExpect(jsonPath("[0].text", is("Text 001")))
        .andExpect(jsonPath("[1].photoUrl", is("Photo url 002")))
        .andExpect(jsonPath("[1].ownerName", is("Owner 002")))
        .andExpect(jsonPath("[1].text", is("Text 002")))
        .andExpect(jsonPath("[2].photoUrl", is("Photo url 003")))
        .andExpect(jsonPath("[2].ownerName", is("Owner 003")))
        .andExpect(jsonPath("[2].text", is("Text 003")));
  }

}
