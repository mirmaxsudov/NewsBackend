package uz.academy.exam.Exam.service.base;

import org.springframework.http.ResponseEntity;
import uz.academy.exam.Exam.model.request.ContactRequest;
import uz.academy.exam.Exam.model.response.response.ApiResponse;

public interface ContactService {
    ResponseEntity<ApiResponse<Void>> createContact(ContactRequest request);
}
