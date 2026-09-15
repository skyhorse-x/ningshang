package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String name;
    
    @Column(nullable = false, length = 20)
    @NotBlank
    @Size(max = 20)
    private String phone;
    
    @Column(length = 100)
    @Size(max = 100)
    @Email
    private String email;
    
    @Column(length = 50)
    @Size(max = 50)
    private String type;
    
    @Column(columnDefinition = "TEXT")
    @NotBlank
    @Size(max = 10000)
    private String content;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
