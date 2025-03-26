package uz.academy.exam.Exam.exceptions;

public class ResendLimitExceededException extends RuntimeException {
    public ResendLimitExceededException(String message) {
        super(message);
    }
}
