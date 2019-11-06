package com.course.springlearning.controller;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@RestController
public class SimpeESController {
    @Autowired
    private TransportClient client;

    @PostMapping("/add/people/man")
    public ResponseEntity add(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "age") int age,
            @RequestParam(name = "country") String country,
            @RequestParam(name = "date")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date
    ) {
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("name", name)
                    .field("age", age)
                    .field("country", country)
                    .field("date", date.getTime())
                    .endObject();
            IndexResponse indexResponse = client.prepareIndex("people", "man").setSource(xContentBuilder).get();
            return new ResponseEntity(indexResponse.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/people/man")
    public ResponseEntity delete(@RequestParam(name = "id") String id) {
        if (id.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        DeleteResponse deleteResponse = client.prepareDelete("people", "man", id).get();
        return new ResponseEntity(deleteResponse.getResult(), HttpStatus.OK);
    }

    @GetMapping("/get/people/man")
    @ResponseBody
    public ResponseEntity get(@RequestParam(name = "id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        GetResponse documentFields = client.prepareGet("people", "man", id).get();
        if (!documentFields.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(documentFields.getSource(), HttpStatus.OK);
    }

    @PutMapping("/update/people/man")
    @ResponseBody
    public ResponseEntity update(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date
    ) {
        if (id.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        UpdateRequest updateRequest = new UpdateRequest("people", "man", id);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            if (name != null) {
                builder.field("name", name);
            }

            if (date != null) {
                builder.field("date", date.getTime());
            }
            builder.endObject();

            updateRequest.doc(builder);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            UpdateResponse result = client.update(updateRequest).get();
            return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @PostMapping("/query/people/man")
    @ResponseBody
    public ResponseEntity query(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date,
            @RequestParam(name = "le_age", required = false) Integer le_age,
            @RequestParam(name = "ge_age", defaultValue = "0") Integer ge_age
    ) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (name != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));
        }

        if(date != null){
            boolQueryBuilder.must(QueryBuilders.matchQuery("date", date));
        }

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from(ge_age);
        if (le_age != null && le_age > 0) {
            rangeQueryBuilder.to(le_age);
        }

        boolQueryBuilder.filter(rangeQueryBuilder);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("people")
                .setTypes("man")
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(10);

        System.out.println(searchRequestBuilder);
        SearchResponse result = searchRequestBuilder.get();

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : result.getHits()) {
            list.add(hit.getSource());
        }

        return new ResponseEntity(list, HttpStatus.OK);
    }
}
