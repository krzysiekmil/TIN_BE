package pjwstk.s20124.tin.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Optional;

public interface CrudApi<T> {

    T create(@Valid T entity);

    T update(@Valid T entity) throws Exception;

    void delete(Long id);

    Optional<T> getOne(Long id);

    Collection<T> getList();


}
