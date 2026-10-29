package net.os.goodcourses.exception;

public class DuplicateFeedbackException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateFeedbackException(String message) {
        super(message);
    }

    public DuplicateFeedbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
