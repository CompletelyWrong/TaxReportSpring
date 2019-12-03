package com.project.reportsystem.exception;

import java.sql.SQLException;

public class DataBaseRuntimeException extends RuntimeException {
    public DataBaseRuntimeException(String message, SQLException e) {
        super(message, e);
    }

    public DataBaseRuntimeException(String message) {
        super(message);
    }

    public DataBaseRuntimeException(Throwable cause) {
        super(cause);
    }
}
