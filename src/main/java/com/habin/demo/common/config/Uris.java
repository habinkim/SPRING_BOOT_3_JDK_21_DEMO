package com.habin.demo.common.config;

public class Uris {

    public static final String API_V1_ROOT = "/v1";

    public static final String REST_NAME_ID = "/{id}";
    public static final String REST_NAME_UUID = "/{uuid}";

    public static final String REGISTER = API_V1_ROOT + "/register";
    public static final String LOGIN = API_V1_ROOT + "/login";

    public static final String[] PERMIT_ALL_URIS = {
        REGISTER,
        LOGIN
    };

    public static final String[] ADMIN_URIS = {

    };

    public static final String[] NOT_LOGGING_URIS = {

    };


}
