package uz.academy.exam.Exam.exceptions;

public class CustomIllegalArgumentException extends RuntimeException {
    public CustomIllegalArgumentException(String message) {
        super(message);
    }
}
