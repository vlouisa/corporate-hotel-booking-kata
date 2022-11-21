package dev.louisa.kata.company.exception;

public class CompanyApiException extends RuntimeException {

    public CompanyApiException (String message) {
        super(message);
    }
}
