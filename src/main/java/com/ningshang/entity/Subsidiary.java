package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "subsidiary")
public class Subsidiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Column(length = 100)
    @Size(max = 100)
    private String englishName;
    
    @Column(length = 50)
    @Size(max = 50)
    private String category;
    
    @Column(columnDefinition = "LONGTEXT")
    @Size(max = 1000000)
    private String description;
    
    @Column(length = 200)
    @Size(max = 200)
    private String logo;
    
    @Column(length = 200)
    @Size(max = 200)
    private String background;
    
    private Integer sortOrder;

    @Transient
    private List<Long> coreBusinessIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
