package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @Column(length = 50)
    @Size(max = 50)
    private String department;
    
    @Column(length = 50)
    @Size(max = 50)
    private String headcount;
    
    @Column(length = 50)
    @Size(max = 50)
    private String location;
    
    @Column(length = 50)
    @Size(max = 50)
    private String education;
    
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
