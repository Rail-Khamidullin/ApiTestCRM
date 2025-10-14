package api.entityRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortOption {
    private String property;  // поле для сортировки (например, "id")
    private String direction; // направление ("ASC" или "DESC")
}
