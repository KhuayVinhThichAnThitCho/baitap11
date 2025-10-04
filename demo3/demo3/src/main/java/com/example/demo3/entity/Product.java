package com.example.demo3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "products")
public class Product {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(columnDefinition = "nvarchar(255)")
  private String name;

  @NotBlank
  @Column(columnDefinition = "nvarchar(255)")
  private String brand;

  @NotBlank
  @Column(name = "made_in", columnDefinition = "nvarchar(255)")
  private String madeIn;

  @Positive
  @Column(precision = 18, scale = 2, nullable = false)
  private BigDecimal price;
}
