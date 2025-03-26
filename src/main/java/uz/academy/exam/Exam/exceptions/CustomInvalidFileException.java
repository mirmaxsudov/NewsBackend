package uz.academy.exam.Exam.exceptions;

public class CustomInvalidFileException extends RuntimeException {
    public CustomInvalidFileException(String message) {
        super(message);
    }
}