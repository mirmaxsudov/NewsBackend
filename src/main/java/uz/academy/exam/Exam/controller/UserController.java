package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.academy.exam.Exam.model.response.UserResponse;
import uz.academy.exam.Exam.service.base.UserService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}