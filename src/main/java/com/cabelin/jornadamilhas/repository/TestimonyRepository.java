package com.cabelin.jornadamilhas.repository;

import com.cabelin.jornadamilhas.model.entity.TestimonyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonyRepository extends JpaRepository<TestimonyEntity, Long> {

  @Query(value = "SELECT * FROM testimony ORDER BY RAND() LIMIT :limit", nativeQuery = true)
  List<TestimonyEntity> findRandomByLimit(int limit);

}
