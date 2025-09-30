package org.jdt16.user4a.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestApiPathUtility {
    public static final String API_PATH = "/api";
    public static final String API_VERSION = "/v1";
    public static final String API_PATH_USER = "/user";
    public static final String API_PATH_BY_USER_ID = "/{id}";
    public static final String API_PATH_CREATE = "/create";
    public static final String API_PATH_UPDATE = "/update";
}
