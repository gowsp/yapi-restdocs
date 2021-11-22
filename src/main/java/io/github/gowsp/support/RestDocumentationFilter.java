package io.github.gowsp.support;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestDocumentationFilter implements Filter {

    private final Class<?> callClass;

    public RestDocumentationFilter(Class<?> callClass) {
        this.callClass = callClass;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext context) {
        Response response = context.next(requestSpec, responseSpec);
        String path = requestSpec.getUserDefinedPath();
        String method = requestSpec.getMethod();
        return response;
    }
}
