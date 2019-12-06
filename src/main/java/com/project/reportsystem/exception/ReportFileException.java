package com.project.reportsystem.exception;

public class ReportFileException extends RuntimeException {
    public ReportFileException(String message) {
        super(message);
    }

    public ReportFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
