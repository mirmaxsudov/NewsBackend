package uz.academy.exam.Exam.exceptions;

public class CustomInternalErrorException extends RuntimeException{

    public CustomInternalErrorException(String message) {
        super(message);
    }
}
