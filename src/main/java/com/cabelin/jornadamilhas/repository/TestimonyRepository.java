package com.cabelin.jornadamilhas.repository;

import com.cabelin.jornadamilhas.model.entity.TestimonyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonyRepository extends JpaRepository<TestimonyEntity, Long> {}
