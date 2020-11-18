package com.backend.search;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class BackendEsTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    void testCreateIndex () throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("Paper_index");
        CreateIndexResponse createIndexResponse =
                restHighLevelClient.indices().create(request,RequestOptions.DEFAULT);
    }

}
