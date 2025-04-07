package uz.academy.exam.Exam.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.repository.post.PostRateRepository;
import uz.academy.exam.Exam.service.base.post.PostRateService;

@Service
@RequiredArgsConstructor
public class IPostRateService implements PostRateService {
    private final PostRateRepository postRateRepository;
}