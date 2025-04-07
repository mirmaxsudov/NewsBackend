package uz.academy.exam.Exam.model.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Initialize with a default value to avoid null
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;

    @PostPersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreRemove
    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
        this.isDeleted = true;
    }
}