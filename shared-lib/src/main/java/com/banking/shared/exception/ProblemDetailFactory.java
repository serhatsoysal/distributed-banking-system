package com.banking.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ProblemDetailFactory {

    public static ProblemDetail createProblemDetail(Exception ex, HttpStatus status, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(ex.getClass().getSimpleName());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    public static ProblemDetail createNotFoundProblem(String resource, Object id) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            String.format("%s with id %s not found", resource, id)
        );
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.banking.com/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("resource", resource);
        problemDetail.setProperty("resourceId", id);
        return problemDetail;
    }

    public static ProblemDetail createBusinessProblem(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY, 
            message
        );
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setType(URI.create("https://api.banking.com/errors/business-rule"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}

