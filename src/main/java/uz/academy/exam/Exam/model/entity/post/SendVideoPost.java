package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.attachment.VideoAttachment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendVideoPost extends Post {
    @OneToMany
    private List<ImageAttachment> imageGallery = new ArrayList<>();
    @ManyToOne
    private ImageAttachment thumbnail;
    @ManyToOne
    private VideoAttachment video;
}