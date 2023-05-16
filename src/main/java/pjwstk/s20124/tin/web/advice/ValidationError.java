package pjwstk.s20124.tin.web.advice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {
    private String objectName;
    private String field;
    private String error;
}
