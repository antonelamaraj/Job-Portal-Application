package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.exception.DateNotValidException;

public interface BaseMapper<E, D> {
    E toEntity(D dto);
    D toDTO(E entity);
}
