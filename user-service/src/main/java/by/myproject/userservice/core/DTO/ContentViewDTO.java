package by.myproject.userservice.core.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ContentViewDTO {
    @Getter
    @Setter
    private int number;
    @Setter
    @Getter
    private int size;
    @Setter
    @Getter
    private int totalPages;
    @Setter
    @Getter
    private int totalElements;
    @Setter
    @Getter
    private boolean first;
    @Setter
    @Getter
    private int number_of_elements;
    @Setter
    @Getter
    private boolean last;
    @Setter
    @Getter
    private List<?> content;
}

