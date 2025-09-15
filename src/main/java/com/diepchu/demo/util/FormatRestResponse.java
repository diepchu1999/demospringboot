package com.diepchu.demo.util;

import com.diepchu.demo.domain.RestResonse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpServletResponse.getStatus();

        //case error
        RestResonse<Object> restResonse = new RestResonse<Object>();
        restResonse.setStatusCode(status);

//        if(body instanceof RestResonse){
//            return body;
//        }
        if (status >= 400) {
            return body;

        }else {
            restResonse.setData(body);
            restResonse.setError("CALL API SUCCEEDED");
        }
        return restResonse;
    }
}
