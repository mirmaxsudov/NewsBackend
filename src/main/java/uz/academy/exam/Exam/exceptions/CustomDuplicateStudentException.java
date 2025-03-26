package uz.academy.exam.Exam.exceptions;

public class CustomDuplicateStudentException extends RuntimeException {

    public CustomDuplicateStudentException(String message) {
        super(message);
    }
}