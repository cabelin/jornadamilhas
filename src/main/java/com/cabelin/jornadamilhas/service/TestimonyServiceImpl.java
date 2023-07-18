package com.cabelin.jornadamilhas.service;

import com.cabelin.jornadamilhas.converter.TestimonyConverter;
import com.cabelin.jornadamilhas.model.dto.TestimonyRequestDto;
import com.cabelin.jornadamilhas.model.dto.TestimonyResponseDto;
import com.cabelin.jornadamilhas.model.entity.TestimonyEntity;
import com.cabelin.jornadamilhas.repository.TestimonyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestimonyServiceImpl implements TestimonyService {

  private static final int RANDOM_SIZE = 3;

  private final TestimonyRepository testimonyRepository;

  @Override
  public TestimonyResponseDto create(TestimonyRequestDto testimonyRequestDto) {
    TestimonyEntity testimonyEntity = testimonyRepository.save(
        TestimonyEntity.builder()
            .photoUrl(testimonyRequestDto.getPhotoUrl())
            .text(testimonyRequestDto.getText())
            .ownerName(testimonyRequestDto.getOwnerName())
            .build()
    );

    return TestimonyResponseDto.builder()
        .id(testimonyEntity.getId())
        .photoUrl(testimonyEntity.getPhotoUrl())
        .text(testimonyEntity.getText())
        .ownerName(testimonyEntity.getOwnerName())
        .build();
  }

  @Override
  public Page<TestimonyResponseDto> getAll(Pageable pageable) {
    return testimonyRepository.findAll(pageable)
        .map(TestimonyConverter::entityToResponse);
  }

  @Override
  public TestimonyResponseDto update(Long id, TestimonyRequestDto testimonyRequestDto) {
    TestimonyEntity testimonyEntity = testimonyRepository.findById(id).orElseThrow();

    testimonyEntity.setPhotoUrl(testimonyRequestDto.getPhotoUrl());
    testimonyEntity.setText(testimonyRequestDto.getText());
    testimonyEntity.setOwnerName(testimonyRequestDto.getOwnerName());

    return TestimonyConverter.entityToResponse(
        testimonyRepository.save(testimonyEntity)
    );
  }

  @Override
  public void remove(Long id) {
    if (testimonyRepository.existsById(id)) {
      testimonyRepository.deleteById(id);
    }
  }

  @Override
  public List<TestimonyResponseDto> getRandomThree() {
    return testimonyRepository.findRandomByLimit(RANDOM_SIZE)
        .stream()
        .map(TestimonyConverter::entityToResponse)
        .toList();
  }

}
