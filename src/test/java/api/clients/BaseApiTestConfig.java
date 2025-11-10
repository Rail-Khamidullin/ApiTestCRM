package api.clients;

import org.junit.jupiter.api.BeforeAll;
import utils.TokenAuthorization;

public class BaseApiTestConfig {

    protected final static String BASE_URI = "https://preprod-crm.sbercity.ru";
    protected static String TOKEN;

    @BeforeAll
    public static void getToken() {

        TOKEN = TokenAuthorization.getToken();
    }
}
