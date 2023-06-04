package pjwstk.s20124.tin.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class InfoChange extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InfoChangeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Animal animalRelatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userRelatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event eventRelatedTo;

    @OneToMany(mappedBy = "infoChange")
    private Set<Comment> comments;

}
