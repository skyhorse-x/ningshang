package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "team_member")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String name;
    
    @Column(length = 100)
    @Size(max = 100)
    private String position;
    
    @Column(columnDefinition = "LONGTEXT")
    @Size(max = 1000000)
    private String description;
    
    @Column(length = 200)
    @Size(max = 200)
    private String avatar;
    
    @Column(length = 50)
    @Size(max = 50)
    private String gradient;
    
    private Integer sortOrder;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
