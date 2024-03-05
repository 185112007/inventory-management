package edu.gafur.inventoryservice.exception;

import edu.gafur.inventoryservice.exception.base.AppBaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AppBaseException {

    public NotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
