package uz.academy.exam.Exam.model.response;

import lombok.*;
import uz.academy.exam.Exam.model.preview.SendPostPreview;
import uz.academy.exam.Exam.model.preview.UserPreview;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostPageResponseOwn {
    private int page;
    private int size;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private List<SendPostPreview> content = new ArrayList<>();
    private UserPreview owner;
}