package com.vpc.rest.api.myretail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class HomeController {

    @GetMapping(value = "/")
    public String index() {

        // This endpoint / is just to redirect to swagger-ui for easy access of swagger
        return "redirect:/swagger-ui.html";
    }

}
