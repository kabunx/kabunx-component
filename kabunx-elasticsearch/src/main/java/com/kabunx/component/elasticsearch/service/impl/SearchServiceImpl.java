package com.kabunx.component.elasticsearch.service.impl;

import com.kabunx.component.common.dto.Page;
import com.kabunx.component.common.dto.Pagination;
import com.kabunx.component.common.dto.SimplePagination;
import com.kabunx.component.common.util.JsonUtils;
import com.kabunx.component.elasticsearch.exception.ElasticsearchException;
import com.kabunx.component.elasticsearch.service.SearchService;
import com.kabunx.component.elasticsearch.util.ReflectionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {
    private final RestHighLevelClient client;

    private final ElasticsearchOperations operations;

    public SearchServiceImpl(RestHighLevelClient client, ElasticsearchOperations operations) {
        this.client = client;
        this.operations = operations;
    }

    @Override
    public <T> List<T> filter(Class<T> clazz, SearchSourceBuilder builder) throws ElasticsearchException {
        SearchResponse response = search(clazz, builder);
        SearchHit[] hits = response.getHits().getHits();
        return hits2Objects(hits, clazz);
    }

    @Override
    public <T> Pagination<T> paginate(Class<T> clazz, SearchSourceBuilder builder, Page page) throws ElasticsearchException {
        builder.from(page.from());
        builder.size(page.getPageSize());
        SearchResponse response = search(clazz, builder);
        SearchHits hits = response.getHits();
        return new Pagination<>(
                page.getPage(),
                hits.getTotalHits().value,
                hits2Objects(hits.getHits(), clazz)
        );
    }

    @Override
    public <T> SimplePagination<T> simplePaginate(Class<T> clazz, SearchSourceBuilder builder, Page page) throws ElasticsearchException {
        builder.from(page.from());
        builder.size(page.getPageSize() + 1);
        SearchResponse response = search(clazz, builder);
        SearchHits hits = response.getHits();
        SearchHit[] cHits = Arrays.copyOfRange(hits.getHits(), 0, page.getPageSize());
        Boolean hasMore = hits.getHits().length > page.getPageSize();
        return new SimplePagination<>(
                hasMore,
                hits2Objects(cHits, clazz)
        );
    }

    @Override
    public <T> long count(Class<T> clazz, SearchSourceBuilder builder) throws ElasticsearchException {
        try {
            CountRequest request = new CountRequest();
            CountResponse response = client.count(request, RequestOptions.DEFAULT);
            return response.getCount();
        } catch (IOException ie) {
            throw new ElasticsearchException(ie.getMessage());
        }
    }

    private <T> SearchResponse search(Class<T> clazz, SearchSourceBuilder sourceBuilder) throws ElasticsearchException {
        String index = ReflectionUtils.getIndexName(clazz);
        if (Objects.isNull(index)) {
            throw new ElasticsearchException("elasticsearch index error");
        }
        SearchRequest request = new SearchRequest(index);
        request.source(sourceBuilder);
        try {
            return client.search(request, RequestOptions.DEFAULT);
        } catch (IOException ie) {
            throw new ElasticsearchException(ie.getMessage());
        }
    }

    private <T> List<T> hits2Objects(SearchHit[] hits, Class<T> clazz) {
        return Arrays.stream(hits)
                .map(hit -> JsonUtils.json2Object(hit.getSourceAsString(), clazz))
                .collect(Collectors.toList());
    }
}
