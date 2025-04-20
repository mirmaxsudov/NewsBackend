//package uz.academy.exam.Exam.runner;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import uz.academy.exam.Exam.model.entity.user.User;
//import uz.academy.exam.Exam.model.enums.UserRole;
//import uz.academy.exam.Exam.service.base.UserService;
//
//@Component
//@RequiredArgsConstructor
//public class InitUserRunner implements CommandLineRunner {
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        User user = User.builder()
//                .firstName("Abdurahmon")
//                .lastName("Mirmaxsudov")
//                .userName("MirmaxsudovAbdurahmon")
//                .email("abdurahmonmirmaxsudov2804@gmail.com")
//                .explanation("<h1>Test uchun</h1>")
//                .role(UserRole.USER)
//                .password(passwordEncoder.encode("123"))
//                .profileImage(null)
//                .bannerImage(null)
//                .build();
//
//        if (userService.findUserByLogin(user.getEmail()).isEmpty()) {
//            userService.save(user);
//        }
//    }
//}