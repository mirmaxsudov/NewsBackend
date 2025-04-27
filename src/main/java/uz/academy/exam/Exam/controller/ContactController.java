package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.academy.exam.Exam.model.request.ContactRequest;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.service.base.ContactService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/contact")
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createContact(
            @RequestBody ContactRequest request
    ) {
        return contactService.createContact(request);
    }
}