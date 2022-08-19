package com.kinnon;

import com.kinnon.domain.DiscussPost;
import com.kinnon.mapper.DiscussPostMapper;
import com.kinnon.mapper.elasticsearch.DiscussPostRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-17 20:06
 */

@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void insert() {
        discussPostRepository.save(discussPostMapper.selectById(241));
        discussPostRepository.save(discussPostMapper.selectById(242));
        discussPostRepository.save(discussPostMapper.selectById(243));
    }

    @Test
    public void insertAll() {
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(101, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(102, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(111, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(112, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(131, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(132, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(133, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDisCussPosts(134, 0, 100));

    }

    @Test
    public void searchTest() {

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                //高亮显示
                .withHighlightFields(

                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")

                ).build();

        Page<DiscussPost> result = discussPostRepository.search(build);
        System.out.println(result.getTotalElements());
        System.out.println(result.getTotalPages());
        System.out.println(result.getNumber());
        System.out.println(result.getSize());
        for (DiscussPost discussPost : result) {
            System.out.println(discussPost);
        }


    }

    @Test
    public void searchTest2() {
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                //高亮显示
                .withHighlightFields(

                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")

                ).build();

        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(build, DiscussPost.class);
        // 得到查询结果返回的内容
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        // 设置一个需要返回的实体类集合
        List<DiscussPost> discussPosts = new ArrayList<>();
        // 遍历返回的内容进行处理
        for (SearchHit<DiscussPost> searchHit : searchHits) {
            // 高亮的内容
            Map<String, List<String>> highLightFields = searchHit.getHighlightFields();
            // 将高亮的内容填充到content中

            searchHit.getContent().setTitle(highLightFields.get("title") == null ? searchHit.getContent().getTitle() : highLightFields.get("title").get(0));

            searchHit.getContent().setContent(highLightFields.get("content") == null ? searchHit.getContent().getContent() : highLightFields.get("content").get(0));
            // 放到实体类中
            discussPosts.add(searchHit.getContent());

        }
        System.out.println(discussPosts.size());
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);

        }


    }

}
