package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.entity.base.Base;
import uz.academy.exam.Exam.model.enums.Category;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Post extends Base {
    @Column(nullable = false, length = 255)
    private String title;

    private Long views = 0L;
    private boolean isDraft = false;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String postBody;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostRate> rates = new ArrayList<>();
}