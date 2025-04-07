package uz.academy.exam.Exam.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.repository.post.PostCommentRepository;
import uz.academy.exam.Exam.service.base.post.PostCommentService;

@Service
@RequiredArgsConstructor
public class IPostCommentService implements PostCommentService {
    private final PostCommentRepository postCommentRepository;
}