package com.cabelin.jornadamilhas.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity(name = "TESTIMONY")
@NoArgsConstructor
@AllArgsConstructor
public class TestimonyEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @Column(name = "photo_url")
  private String photoUrl;

  @Column(name = "text")
  private String text;

  @Column(name = "owner_name")
  private String ownerName;

}
