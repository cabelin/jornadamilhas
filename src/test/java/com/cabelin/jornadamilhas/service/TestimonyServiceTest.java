package com.cabelin.jornadamilhas.service;

import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.model.entity.TestimonyEntity;
import com.cabelin.jornadamilhas.repository.TestimonyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestimonyServiceTest {

  @InjectMocks
  private TestimonyServiceImpl testimonyService;

  @Mock
  private TestimonyRepository testimonyRepository;

  @ParameterizedTest
  @CsvSource({
      "anyPhotoUrl, anyText, anyOwnerName",
      "anyPhotoUrl2, anyText2, anyOwnerName2",
      "anyPhotoUrl3, anyText3, anyOwnerName3"
  })
  public void createTestimony_whenSave_thenReturnSavedElement(
      String photoUrl, String text, String ownerName
  ) {
    TestimonyRequestDto testimonyRequestDto = TestimonyRequestDto.builder()
        .photoUrl(photoUrl)
        .text(text)
        .ownerName(ownerName)
        .build();

    when(testimonyRepository.save(any(TestimonyEntity.class)))
        .thenReturn(TestimonyEntity.builder()
            .id(59L)
            .photoUrl(photoUrl)
            .text(text)
            .ownerName(ownerName)
            .build());

    TestimonyResponseDto testimonyResponseDto = testimonyService.create(testimonyRequestDto);

    ArgumentCaptor<TestimonyEntity> testimonyEntityArgumentCaptor = ArgumentCaptor.forClass(TestimonyEntity.class);
    verify(testimonyRepository).save(testimonyEntityArgumentCaptor.capture());

    TestimonyEntity testimonyEntity = testimonyEntityArgumentCaptor.getValue();
    assertNull(testimonyEntity.getId());
    assertThat(testimonyEntity.getPhotoUrl(), is(photoUrl));
    assertThat(testimonyEntity.getText(), is(text));
    assertThat(testimonyEntity.getOwnerName(), is(ownerName));

    assertThat(testimonyResponseDto.getId(), is(59L));
    assertThat(testimonyResponseDto.getPhotoUrl(), is(photoUrl));
    assertThat(testimonyResponseDto.getText(), is(text));
    assertThat(testimonyResponseDto.getOwnerName(), is(ownerName));
  }

  @Test
  public void getAllTestimony_whenFind_thenReturnAllElement() {
    List<TestimonyEntity> testimonyEntities = Arrays.asList(
        TestimonyEntity.builder()
            .id(39L)
            .photoUrl("Photo url 001")
            .ownerName("Owner 001")
            .text("Text 001")
            .build(),
        TestimonyEntity.builder()
            .id(40L)
            .photoUrl("Photo url 002")
            .ownerName("Owner 002")
            .text("Text 002")
            .build()
    );
    Page<TestimonyEntity> testimonyEntityPage = new PageImpl<>(testimonyEntities);

    when(testimonyRepository.findAll(any(Pageable.class)))
        .thenReturn(testimonyEntityPage);

    Pageable pageableRequest = PageRequest.of(0, 10);
    Page<TestimonyResponseDto> testimonyResponseDtos = testimonyService.getAll(pageableRequest);

    ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(testimonyRepository).findAll(pageableArgumentCaptor.capture());

    Pageable pageable = pageableArgumentCaptor.getValue();
    assertNotNull(pageable);
    assertThat(pageable.getPageNumber(), is(0));
    assertThat(pageable.getPageSize(), is(10));

    assertThat(testimonyResponseDtos.getTotalElements(), is(2L));
    assertThat(testimonyResponseDtos.getTotalPages(), is(1));
    assertThat(testimonyResponseDtos.getSize(), is(2));
    assertThat(testimonyResponseDtos.getNumberOfElements(), is(2));

    TestimonyResponseDto firstTestimonyResponseDto = testimonyResponseDtos.get().findFirst().orElseThrow();

    assertNotNull(firstTestimonyResponseDto);
    assertThat(firstTestimonyResponseDto.getId(), is(39L));
    assertThat(firstTestimonyResponseDto.getPhotoUrl(), is("Photo url 001"));
    assertThat(firstTestimonyResponseDto.getText(), is("Text 001"));
    assertThat(firstTestimonyResponseDto.getOwnerName(), is("Owner 001"));
  }

  @ParameterizedTest
  @CsvSource({
      "59, anyPhotoUrl, anyText, anyOwnerName",
      "29, anyPhotoUrl2, anyText2, anyOwnerName2",
      "39, anyPhotoUrl3, anyText3, anyOwnerName3"
  })
  public void updateTestimony_whenSave_thenReturnSavedElement(
      Long id, String photoUrl, String text, String ownerName
  ) {
    TestimonyRequestDto testimonyRequestDto = TestimonyRequestDto.builder()
        .photoUrl(photoUrl)
        .text(text)
        .ownerName(ownerName)
        .build();

    TestimonyEntity testimonyEntitySaved = TestimonyEntity.builder()
        .id(id)
        .photoUrl("photo")
        .text("text")
        .ownerName("ownerName")
        .build();

    when(testimonyRepository.findById(id)).thenReturn(Optional.of(testimonyEntitySaved));

    when(testimonyRepository.save(any(TestimonyEntity.class)))
        .thenReturn(TestimonyEntity.builder()
            .id(id)
            .photoUrl(photoUrl)
            .text(text)
            .ownerName(ownerName)
            .build());

    TestimonyResponseDto testimonyResponseDto = testimonyService.update(id, testimonyRequestDto);

    ArgumentCaptor<TestimonyEntity> testimonyEntityArgumentCaptor = ArgumentCaptor.forClass(TestimonyEntity.class);
    verify(testimonyRepository).save(testimonyEntityArgumentCaptor.capture());

    TestimonyEntity testimonyEntity = testimonyEntityArgumentCaptor.getValue();
    assertThat(testimonyEntity.getId(), is(id));
    assertThat(testimonyEntity.getPhotoUrl(), is(photoUrl));
    assertThat(testimonyEntity.getText(), is(text));
    assertThat(testimonyEntity.getOwnerName(), is(ownerName));

    assertThat(testimonyResponseDto.getId(), is(id));
    assertThat(testimonyResponseDto.getPhotoUrl(), is(photoUrl));
    assertThat(testimonyResponseDto.getText(), is(text));
    assertThat(testimonyResponseDto.getOwnerName(), is(ownerName));
  }

}
