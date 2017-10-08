package com.example.hbase_messaging.controller;

import com.example.hbase_messaging.request.seeds.PostSeedsRequest;
import com.example.hbase_messaging.service.SeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class SeedsController {

    private SeedsService seedsService;

    @Autowired
    public SeedsController(SeedsService seedsService) {
        this.seedsService = seedsService;
    }

    @RequestMapping(path = "/seeds", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestBody @Validated PostSeedsRequest request) {
        seedsService.post(request);
    }
}
