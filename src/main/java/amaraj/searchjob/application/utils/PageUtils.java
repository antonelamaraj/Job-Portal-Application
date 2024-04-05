package amaraj.searchjob.application.utils;

import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.mapper.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class PageUtils {
    public static <E,D> PageDTO<D> toPageImpl(Page<E> page, BaseMapper<E,D> mapper){
        List<D> entities = page.getContent().stream().map(o -> mapper.toDTO(o)).collect(Collectors.toList());
        PageDTO<D> output = new PageDTO<>(entities,page.getTotalElements(),page.getTotalPages(),page.getSize(),page.isFirst(),page.isLast());
        return output;
    }
}
