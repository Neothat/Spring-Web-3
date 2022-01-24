package com.geekbrains.spring.web.endpoints;

import com.geekbrains.spring.web.services.ProductsService;
import com.geekbrains.spring.web.soap.products.GetAllProductRequest;
import com.geekbrains.spring.web.soap.products.GetAllProductResponse;
import com.geekbrains.spring.web.soap.products.GetProductByIdRequest;
import com.geekbrains.spring.web.soap.products.GetProductByIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {
    private static final String NAMESPACE_URI = "http://www.geekbrains.com/spring/ws/products";
    private final ProductsService productsService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProductByIdRequest")
    @ResponsePayload
    public GetProductByIdResponse getStudentByName(@RequestPayload GetProductByIdRequest request) {
        GetProductByIdResponse response = new GetProductByIdResponse();
        response.setProduct(productsService.soapFindById(request.getId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllProductRequest")
    @ResponsePayload
    public GetAllProductResponse getAllProduct(@RequestPayload GetAllProductRequest request) {
        GetAllProductResponse response = new GetAllProductResponse();
        productsService.soapFindAll().forEach(response.getProduct()::add);
        return response;
    }

    /*
        POST http://localhost:8080/ws
        Header -> Content-Type: text/xml

        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:f="http://www.geekbrains.com/spring/ws/products">
            <soapenv:Header/>
            <soapenv:Body>
                <f:GetAllProductRequest/>
            </soapenv:Body>
        </soapenv:Envelope>

        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
          xmlns:f="http://www.geekbrains.com/spring/ws/products">
            <soapenv:Header/>
            <soapenv:Body>
                <f:GetProductByIdRequest>
                    <f:id>1</f:id>
                </f:GetProductByIdRequest>
            </soapenv:Body>
        </soapenv:Envelope>

     */
}
