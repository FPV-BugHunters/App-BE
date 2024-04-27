package com.umb.tradingapp.controller;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.umb.tradingapp.security.controller.HttpServletRequest;

import java.io.IOException;

@RestController
public class RootController {
    

    private final Resource indexHtml = new ClassPathResource("static/index.html");

    @GetMapping(value = "/")
    @ResponseBody
    public Resource serveFrontend(HttpServletRequest request) throws IOException {
        return indexHtml;
    }
    
}
