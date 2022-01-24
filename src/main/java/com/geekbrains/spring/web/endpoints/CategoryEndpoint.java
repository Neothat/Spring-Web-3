package com.geekbrains.spring.web.endpoints;

import com.geekbrains.spring.web.services.CategoryService;
import com.geekbrains.spring.web.soap.categories.GetCategoryByNameRequest;
import com.geekbrains.spring.web.soap.categories.GetCategoryByNameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class CategoryEndpoint {

    private static final String NAMESPACE_URI = "http://www.geekbrains.com/spring/ws/categories";
    private final CategoryService categoryService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCategoryByNameRequest")
    @ResponsePayload
    public GetCategoryByNameResponse getCategoryByName(@RequestPayload GetCategoryByNameRequest request) {
        GetCategoryByNameResponse response = new GetCategoryByNameResponse();
        response.setCategory(categoryService.soapFindByName(request.getName()));
        return response;
    }
}
