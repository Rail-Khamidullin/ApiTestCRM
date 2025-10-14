package utils;

import org.junit.jupiter.api.BeforeAll;

public class BaseApiTestConfig {

//    protected final ObjectMapper mapper = new ObjectMapper();
//    {
//
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        JavaTi
//        JavaTimeModule module = new JavaTimeModule();
//        mapper.registerModule(module);
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//    }
    protected final static String BASE_URI = "https://preprod-crm.sbercity.ru";
    protected static String TOKEN;

    @BeforeAll
    public static void getToken() {

        TOKEN = TokenAuthorization.getToken();
    }
}
