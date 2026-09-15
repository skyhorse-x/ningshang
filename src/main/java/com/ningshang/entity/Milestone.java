package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "milestone")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(nullable = false, length = 20)
    @NotBlank
    @Size(max = 20)
    private String year;
    
    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @Column(columnDefinition = "LONGTEXT")
    @Size(max = 1000000)
    private String description;
    
    private Integer sortOrder;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
