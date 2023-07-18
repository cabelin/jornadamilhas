package com.cabelin.jornadamilhas.controller;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.service.TestimonyService;
import com.cabelin.jornadamilhas.utils.FileUtil;
import com.cabelin.jornadamilhas.utils.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TestimonyControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TestimonyService testimonyService;

  public static class Paths {
    private static final String BASE_PATH = "/depoimentos";
    private static final String PUT_URL_TEMPLATE = BASE_PATH + "/{id}";
    private static final String DELETE_URL_TEMPLATE = BASE_PATH + "/{id}";

    public static String postUrl() {
      return BASE_PATH;
    }

    public static String getUrl() {
      return BASE_PATH;
    }

    public static String putUrl(Long id) {
      return PUT_URL_TEMPLATE.replace("{id}", String.valueOf(id));
    }

    public static String deleteUrl(Long id) {
      return DELETE_URL_TEMPLATE.replace("{id}", String.valueOf(id));
    }

  }

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

    mvc.perform(post(Paths.postUrl())
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
    mvc.perform(post(Paths.postUrl())
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

    mvc.perform(post(Paths.postUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .content(MapperUtil.serializeTestimonyRequestDto(dto)))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void getAllTestimony_whenHasZeroTestimony_thenStatus200WithNoElements() throws Exception {
    given(testimonyService.getAll(any(Pageable.class)))
        .willReturn(Page.empty());

    mvc.perform(get(Paths.getUrl())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("content", empty()))
        .andExpect(jsonPath("size", is(0)))
        .andExpect(jsonPath("totalElements", is(0)));
  }

  @ParameterizedTest
  @MethodSource("providePageableParameters")
  public void getAllTestimony_whenHasTestimonies_thenStatus200WithElements(int page, int size, int expectedTestimonyCount) throws Exception {
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
            .build()
    );
    Page<TestimonyResponseDto> testimonyResponseDtoPage = new PageImpl<>(testimonyResponseDtos);

    when(testimonyService.getAll(any(Pageable.class))).thenReturn(testimonyResponseDtoPage);

    mvc.perform(get(Paths.getUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(expectedTestimonyCount)))
        .andExpect(jsonPath("$.content[0].photoUrl", is("Photo url 001")))
        .andExpect(jsonPath("$.content[0].ownerName", is("Owner 001")))
        .andExpect(jsonPath("$.content[0].text", is("Text 001")));

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(testimonyService).getAll(pageableCaptor.capture());

    Pageable pageable = pageableCaptor.getValue();
    assertThat(pageable.getPageNumber()).isEqualTo(page);
    assertThat(pageable.getPageSize()).isEqualTo(size);
  }

  @Test
  public void updateTestimony_whenPutTestimony_thenStatus200() throws Exception {
    String testimonyJson = FileUtil.readFromFileToString("/files/testimony.json");
    TestimonyRequestDto testimonyRequestDto = MapperUtil.deserializeTestimonyRequestDto(testimonyJson);

    Long id = 49L;

    given(testimonyService.update(any(Long.class), any(TestimonyRequestDto.class)))
        .willReturn(TestimonyResponseDto.builder()
            .id(id)
            .photoUrl(testimonyRequestDto.getPhotoUrl())
            .text(testimonyRequestDto.getText())
            .ownerName(testimonyRequestDto.getOwnerName())
            .build());

    mvc.perform(put(Paths.putUrl(id))
            .contentType(MediaType.APPLICATION_JSON)
            .content(testimonyJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(49)))
        .andExpect(jsonPath("photoUrl", is("http://localhost:8080/img")))
        .andExpect(jsonPath("text", is("depoimento fictício")))
        .andExpect(jsonPath("ownerName", is("José")));
  }

  @Test
  public void putTestimony_whenPutTestimonyNoBody_thenStatus400() throws Exception {
    mvc.perform(put(Paths.putUrl(39L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @ParameterizedTest
  @CsvSource({",anyText,anyOwnerName", "anyPhotoUrl,,anyOwnerName", "anyPhotoUrl,anyText,", ",,"})
  public void putTestimony_whenPutTestimonyInvalidBody_thenStatus400(
      String photoUrl, String text, String ownerName
  ) throws Exception {
    TestimonyRequestDto dto = TestimonyRequestDto.builder()
        .photoUrl(photoUrl)
        .text(text)
        .ownerName(ownerName)
        .build();

    Long id = 69L;

    mvc.perform(put(Paths.putUrl(id))
            .contentType(MediaType.APPLICATION_JSON)
            .content(MapperUtil.serializeTestimonyRequestDto(dto)))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void deleteTestimony_whenDeleteTestimony_thenStatus200() throws Exception {
    Long id = 49L;

    doNothing().when(testimonyService).remove(id);

    mvc.perform(delete(Paths.deleteUrl(id))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  private static Stream<Arguments> providePageableParameters() {
    return Stream.of(
        Arguments.of(0, 10, 2),
        Arguments.of(1, 5, 2),
        Arguments.of(2, 8, 2)
    );
  }

}
