package amaraj.searchjob.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class PageDTO<D> {
    //SHIKO KLASEN E PAGEUTILS

    /*
    nese duam qe mos te na dal kontenti shume i gjate dhe me elemente te panevojshme
    * **/
    private List<D> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private Boolean last;
    private Boolean first;


}
