package uz.academy.exam.Exam.exceptions;

public class CustomAlreadyExistException extends RuntimeException {
    public CustomAlreadyExistException(String message) {
        super(message);
    }
}