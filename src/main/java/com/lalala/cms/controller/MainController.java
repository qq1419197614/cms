package com.lalala.cms.controller;

import com.lalala.cms.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION = "my_book";

    @GetMapping(value = "/getFenlei")
    public List<String> getFenlei() {
        Query query = new Query();
        List<String> fenlei = mongoTemplate.findDistinct(query, "fenlei", COLLECTION, String.class);
        return fenlei;
    }

    @GetMapping(value = "/getLiebiao")
    public PageImpl getLiebiao(@RequestParam(required = true) String fenlei, @RequestParam(required = true) Integer pageNo, @RequestParam(required = true) Integer pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fenlei").is(fenlei));
        query.fields().exclude("content");
        //Sort sort = Sort.by(Sort.Direction.DESC, "_id");
        //Pageable pageable = PageRequest.of(page, count, sort);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        long count = mongoTemplate.count(query, COLLECTION);
        List<Result> list = mongoTemplate.find(query.with(pageable), Result.class, COLLECTION);
        return new PageImpl<Result>(list, pageable, count);
    }

    @GetMapping(value = "/getById")
    public Result getById(@RequestParam(required = true) String id) {
        return mongoTemplate.findById(new ObjectId(id), Result.class, COLLECTION);
    }
}
