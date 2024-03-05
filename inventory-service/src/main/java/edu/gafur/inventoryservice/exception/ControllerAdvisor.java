package edu.gafur.inventoryservice.exception;

import edu.gafur.inventoryservice.exception.base.AppBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {
    @ExceptionHandler(AppBaseException.class)
    public ResponseEntity<String> handleException(AppBaseException ex){
      log.error(ex.getMessage(), ex);
      return ResponseEntity.status(ex.getStatus())
              .body(ex.getMessage());
    }
}
