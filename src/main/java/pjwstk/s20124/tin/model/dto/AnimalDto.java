package pjwstk.s20124.tin.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pjwstk.s20124.tin.model.AnimalType;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalDto {
    private Long id;
    @NotNull
    private String name;
    @Size(max = 255)
    private String description;
    private Date dateOfBirth;
    private AnimalType type;
    private boolean owner = false;
    private String image;
}
