package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Тело запроса пользователя
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJSON {
    private String email;
    private String password;
    private Boolean rememberMe;
}


