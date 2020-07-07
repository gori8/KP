package com.example.webshop.repository;

import com.example.webshop.model.RadElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RadElasticRepository extends ElasticsearchRepository<RadElastic, String> {

    public Iterable<RadElastic> findAllByNaslov(String naslov);
    public Iterable<RadElastic> findAllByApstrakt(String apstrakt);
    public Iterable<RadElastic> findAllByKljucneReci(String kljucneReci);
    public Iterable<RadElastic> findAllByAutor(String autor);
    public Iterable<RadElastic> findAllByNaucnaOblast(String naucnaOblast);
    public Iterable<RadElastic> findAllBySadrzaj(String sadrzaj);
    public Iterable<RadElastic> findAllByNazivCasopisa(String nazivCasopisa);

}
