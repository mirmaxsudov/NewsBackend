package uz.academy.exam.Exam.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.repository.post.SendVideoPostRepository;
import uz.academy.exam.Exam.service.base.post.SendVideoPostService;

@Service
@RequiredArgsConstructor
public class ISendVideoPostService implements SendVideoPostService {
    private final SendVideoPostRepository sendVideoPostRepository;
}