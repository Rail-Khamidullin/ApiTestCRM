package api.entityRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class entityRequest {
    private FilterRequest filter;
    private int page;
    private int size;
    private List<SortOption> sortBy;
}
