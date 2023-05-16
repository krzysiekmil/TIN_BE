package pjwstk.s20124.tin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
abstract public class VersionEntity extends AbstractEntity{

    @JsonIgnore
    @Version
    public int version;
}
