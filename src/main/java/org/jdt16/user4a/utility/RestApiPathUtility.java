package org.jdt16.user4a.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestApiPathUtility {
    public static final String API_PATH_USER = "/user";
    public static final String API_PATH_MODULE_UPDATE_STATUS = "/update/{id}";
}
