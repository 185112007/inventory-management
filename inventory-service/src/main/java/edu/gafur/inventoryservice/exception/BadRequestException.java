package edu.gafur.inventoryservice.exception;

import edu.gafur.inventoryservice.exception.base.AppBaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AppBaseException {

    public BadRequestException(String message, HttpStatus status) {
        super(message, status);
    }
}
