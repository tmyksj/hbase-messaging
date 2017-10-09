package com.example.hbase_messaging.controller;

import com.example.hbase_messaging.request.seeds.PostSeedsRequest;
import com.example.hbase_messaging.service.SeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeedsController {

    private SeedsService seedsService;

    @Autowired
    public SeedsController(SeedsService seedsService) {
        this.seedsService = seedsService;
    }

    @RequestMapping(path = "/seeds", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@Validated PostSeedsRequest request) {
        seedsService.post(request);
    }
}
