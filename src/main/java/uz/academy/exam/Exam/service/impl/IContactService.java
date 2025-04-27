package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.model.entity.Contact;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.request.ContactRequest;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.repository.ContactRepository;
import uz.academy.exam.Exam.service.base.AttachmentService;
import uz.academy.exam.Exam.service.base.ContactService;
import uz.academy.exam.Exam.service.bot.TelegramBot;

@Service
@RequiredArgsConstructor
public class IContactService implements ContactService {
    private final ContactRepository contactRepository;
    private final AttachmentService attachmentService;
    private final TelegramBot telegramBot;

    @Override
    public ResponseEntity<ApiResponse<Void>> createContact(ContactRequest request) {
        ImageAttachment image = (ImageAttachment) attachmentService.getById(request.getImageId());

        Contact contact = Contact.builder()
                .email(request.getEmail())
                .name(request.getName())
                .subject(request.getSubject())
                .explanation(request.getExplanation())
                .image(image)
                .build();

        contactRepository.save(contact);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .data(null)
                        .message("Contact created successfully")
                        .build());
    }
}