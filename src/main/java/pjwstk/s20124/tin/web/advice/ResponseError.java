package pjwstk.s20124.tin.web.advice;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ResponseError {
    private int code;
    private String status;
    private Object errors;
}
