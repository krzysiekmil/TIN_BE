package pjwstk.s20124.tin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

    @CreatedDate
    @JsonIgnoreProperties(allowGetters = true)
    @Column(name = "created_date", updatable = false)
    private Date createDate = Date.from(Instant.now());

    @CreatedBy
    @JsonIgnoreProperties(allowGetters = true)
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @JsonIgnoreProperties(allowGetters = true)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @JsonIgnoreProperties(allowGetters = true)
    @Column(name = "last_modified_date")
    private Date lastModifiedDate = Date.from(Instant.now());

    @PrePersist
    public void prePersist(){
        this.createDate = Date.from(Instant.now());
        this.createdBy = SecurityUtils.getCurrentUserLogin().orElse("SYSTEM");
    }

    @PreUpdate
    public void preUpdate(){
        this.lastModifiedDate = Date.from(Instant.now());
        this.lastModifiedBy = SecurityUtils.getCurrentUserLogin().orElse("SYSTEM");
    }

}
