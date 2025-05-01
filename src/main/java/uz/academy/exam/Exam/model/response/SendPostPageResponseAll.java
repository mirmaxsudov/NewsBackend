package uz.academy.exam.Exam.model.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostPageResponseAll {
    private int page;
    private int size;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private List<SendPostPreviewForAll> content = new ArrayList<>();
}