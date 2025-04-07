package uz.academy.exam.Exam.model.entity.attachment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("VIDEO")
public class VideoAttachment extends Attachment {
    private Long duration;
    private String resolution;
    private String format;
    private String videoUrl;
    @ManyToOne
    private ImageAttachment thumbnail;
}