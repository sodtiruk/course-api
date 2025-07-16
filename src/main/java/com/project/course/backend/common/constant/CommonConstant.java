package com.project.course.backend.common.constant;

public class CommonConstant {

    private CommonConstant() {}

    public static class ErrorMessage {

        private ErrorMessage() {}

        public static final String INTERNAL_SERVER_ERROR = "internal_server_error";
        public static final String BAD_REQUEST = "bad_request";
        public static final String UNAUTHORIZED = "unauthorized";
        public static final String FORBIDDEN = "forbidden";
        public static final String NOT_FOUND = "not_found";
        public static final String SUCCESS = "success";
    }

    public static class StatusCode {

        private StatusCode() {}

        public static final Integer SUCCESS_CODE = 200;
        public static final Integer CREATED_CODE = 201;
        public static final Integer BAD_REQUEST_CODE = 400;
        public static final Integer UNAUTHORIZED_CODE = 401;
        public static final Integer FORBIDDEN_CODE = 403;
        public static final Integer NOT_FOUND_CODE = 404;
        public static final Integer INTERNAL_SERVER_ERROR_CODE = 500;
    }




}
