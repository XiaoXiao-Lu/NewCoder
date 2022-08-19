package com.kinnon.mapper.elasticsearch;

import com.kinnon.domain.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kinnon
 * @create 2022-08-17 20:04
 */

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
