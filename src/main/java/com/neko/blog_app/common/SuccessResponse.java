package com.neko.blog_app.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse implements ApiResponse {
    private boolean success = true;
    private String message;
    private int code;
    private Object metadata;

    public SuccessResponse() {}

    public SuccessResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public SuccessResponse(String message, HttpStatus status, Object metadata) {
        this.message = message;
        this.code = status.value();
        this.metadata = metadata;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.valueOf(code);
    }

    @Override
    public Object getMetadata() {
        return metadata;
    }

    @Override
    public String getMessage() {
        return message;
    }
}