package com.cliente.util;

import spark.Filter;
import spark.Request;
import spark.Response;

public class FilterController {

    public static Filter slash = (Request request, Response response) -> {
        String path = request.pathInfo();
        if (!path.equals("/")) {
            if (path.endsWith("/")) {
                response.redirect(path.substring(0, path.length() - 1));
            }
        }
    };

}
