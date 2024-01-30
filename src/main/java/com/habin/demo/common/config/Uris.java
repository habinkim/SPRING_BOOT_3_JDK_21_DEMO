package com.habin.demo.common.config;

public class Uris {

    public static final String API_V1_ROOT = "/v1";

    public static final String REGISTER = API_V1_ROOT + "/register";
    public static final String LOGIN = API_V1_ROOT + "/login";
    public static final String LOGOUT = API_V1_ROOT + "/logout";

    public static final String REST_NAME_ID = "/{id}";
    public static final String REST_NAME_UUID = "/{uuid}";

    public static final String SWAGGER_UI_ROOT = "/swagger-ui";
    public static final String SWAGGER_UI = "/swagger-ui.html";

    public static final String SWAGGER_INDEX_HTML = SWAGGER_UI_ROOT + "/index.html";
    public static final String SWAGGER_INDEX_CSS = SWAGGER_UI_ROOT + "/index.css";
    public static final String SWAGGER_BUNDLE_JS = SWAGGER_UI_ROOT + "/swagger-ui-bundle.js";
    public static final String SWAGGER_CSS = SWAGGER_UI_ROOT + "/swagger-ui.css";
    public static final String SWAGGER_STANDALONE_PRESET_JS = SWAGGER_UI_ROOT + "/swagger-ui-standalone-preset.js";
    public static final String SWAGGER_INITIALIZER_JS = SWAGGER_UI_ROOT + "/swagger-initializer.js";
    public static final String SWAGGER_FAVICON = SWAGGER_UI_ROOT + "/favicon-32x32.png";

    public static final String SWAGGER_API_DOCS = "/v3/api-docs";
    public static final String SWAGGER_API_DOCS_SWAGGER_CONFIG = SWAGGER_API_DOCS + "/swagger-config";

    public static final String[] PERMIT_ALL_URIS = {
            REGISTER,
            LOGIN,
            SWAGGER_UI,
            SWAGGER_INDEX_HTML,
            SWAGGER_INDEX_CSS,
            SWAGGER_BUNDLE_JS,
            SWAGGER_CSS,
            SWAGGER_STANDALONE_PRESET_JS,
            SWAGGER_INITIALIZER_JS,
            SWAGGER_API_DOCS,
            SWAGGER_API_DOCS_SWAGGER_CONFIG,
            SWAGGER_FAVICON
    };

    public static final String[] ADMIN_URIS = {

    };

    public static final String[] NOT_LOGGING_URIS = {
            SWAGGER_UI,
            SWAGGER_INDEX_HTML,
            SWAGGER_INDEX_CSS,
            SWAGGER_BUNDLE_JS,
            SWAGGER_CSS,
            SWAGGER_STANDALONE_PRESET_JS,
            SWAGGER_INITIALIZER_JS,
            SWAGGER_API_DOCS,
            SWAGGER_API_DOCS_SWAGGER_CONFIG,
            SWAGGER_FAVICON
    };


}
