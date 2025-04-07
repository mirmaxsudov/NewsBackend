package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.academy.exam.Exam.model.entity.User;
import uz.academy.exam.Exam.model.entity.base.Base;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MappedSuperclass
public abstract class Post extends Base {
    @Column(nullable = false, length = 255)
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String postBody;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags = new ArrayList<>();
    @ManyToOne
    private User owner;
}