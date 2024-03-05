package edu.gafur.inventoryservice.exception.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AppBaseException extends RuntimeException{
    private HttpStatus status;

    public AppBaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
