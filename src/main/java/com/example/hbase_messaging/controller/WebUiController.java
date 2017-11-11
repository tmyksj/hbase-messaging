package com.example.hbase_messaging.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebUiController {
    @RequestMapping(path = "/webui", method = RequestMethod.GET)
    public String get() {
        return "webui/index";
    }
}
