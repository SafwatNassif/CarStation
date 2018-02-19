package com.example.safwat.carstation.Model;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by safwat on 10/07/17.
 */

public class RequestBodyAndBody {
    private RequestBody requestBody;
    private Request request ;

    public RequestBodyAndBody(RequestBody requestBody, Request request) {
        this.requestBody = requestBody;
        this.request = request;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
