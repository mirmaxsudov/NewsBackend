package uz.academy.exam.Exam.exceptions;

public class CustomInvalidDtoException extends RuntimeException {
    public CustomInvalidDtoException(String message) {
        super(message);
    }
}