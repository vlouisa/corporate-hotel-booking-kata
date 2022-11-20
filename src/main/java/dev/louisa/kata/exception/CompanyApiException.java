package dev.louisa.kata.exception;

public class CompanyApiException extends RuntimeException {

    public CompanyApiException (String message) {
        super(message);
    }
}
