package com.penguinshop.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String reviewText;

    @NotNull
    private String rating;

    @ElementCollection
    private List<String> productImages;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable=false)
    private Product product;   

    @ManyToOne
    @NotNull
    @JoinColumn(nullable=false)
    private User user;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    

    
}
