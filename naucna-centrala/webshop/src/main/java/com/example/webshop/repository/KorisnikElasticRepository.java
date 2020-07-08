package com.example.webshop.repository;

import com.example.webshop.model.KorisnikElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface KorisnikElasticRepository extends ElasticsearchRepository<KorisnikElastic, String> {

}
