package uz.academy.exam.Exam.model.entity.attachment;

import jakarta.persistence.*;
import lombok.*;
import uz.academy.exam.Exam.model.enums.AttachmentType;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "attachment_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
    private String extension;
    @Enumerated(EnumType.STRING)
    private AttachmentType attachmentType;
}