package io.github.AndCandido.storemanager.api.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseError {

    private List<String> errors;
    private int status;
    private String errorType;
    private LocalDateTime timestamp;

    {
        this.status = HttpStatus.BAD_REQUEST.value();
        this.errorType = HttpStatus.BAD_REQUEST.name();
        this.timestamp = LocalDateTime.now();
    }

    public ResponseError(List<String> errors) {
       this.errors = errors;
    }

    public ResponseError(String error) {
       this.errors = List.of(error);
    }

    public ResponseError(List<String> errors, HttpStatus httpStatus) {
        this(errors);
        this.status = httpStatus.value();
        this.errorType = httpStatus.name();
    }

    public ResponseError(String error, HttpStatus httpStatus) {
        this(List.of(error));
        this.status = httpStatus.value();
        this.errorType = httpStatus.name();
    }
}
