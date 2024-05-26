package com.umb.tradingapp.controller;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.umb.tradingapp.security.controller.HttpServletRequest;

import java.io.IOException;

@RestController
public class RootController {
    


    private final Resource indexHtml = new FileSystemResource("static/index.html");


    @GetMapping(value={"/", "/dashboard", "/watchlist", "/portfolio", "/login"})
    @ResponseBody
    public Resource serveFrontend(HttpServletRequest request) throws IOException {
        return indexHtml;
    }
    
    
    
}
