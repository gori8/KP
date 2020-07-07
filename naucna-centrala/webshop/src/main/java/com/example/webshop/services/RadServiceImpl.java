package com.example.webshop.services;

import com.example.webshop.dto.ElementDTO;
import com.example.webshop.dto.NaucnaOblastDTO;
import com.example.webshop.dto.RadDTO;
import com.example.webshop.dto.RadFoundDTO;
import com.example.webshop.model.*;
import com.example.webshop.repository.*;
import com.example.webshop.util.storage.StorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class RadServiceImpl implements RadService {

    @Autowired
    IzdanjeRepository izdanjeRepository;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    RadRepository radRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    RadElasticRepository radElasticRepository;

    @Autowired
    ElasticsearchRestTemplate operations;

    @Override
    public List<NaucnaOblastDTO> getNaucneOblastiByIzdanjeId(Long izdanjeId){
        Izdanje izdanje = izdanjeRepository.getOne(izdanjeId);

        List<NaucnaOblastDTO> ret = new ArrayList<>();

        for (NaucnaOblast oblast : izdanje.getCasopis().getNaucneOblasti()) {
            NaucnaOblastDTO dto = new NaucnaOblastDTO();

            dto.setNaziv(oblast.getNaziv());
            dto.setId(oblast.getId());

            ret.add(dto);
        }

        return ret;
    }

    @Override
    public Long addScientificWork(RadDTO dto){
        Izdanje izdanje = izdanjeRepository.getOne(dto.getIzdanjeId());
        NaucnaOblast naucnaOblast = naucnaOblastRepository.getOne(dto.getNaucnaOblast());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Korisnik autor = korisnikRepository.findOneByUsername(username);

        List<Korisnik> recenzenti = izdanje.getCasopis().getRecenzenti();

        Rad rad = new Rad();

        rad.setNaslov(dto.getNaslov());
        rad.setApstrakt(dto.getApstrakt());
        rad.setKljucneReci(dto.getKljucneReci());
        rad.setSadrzaj(dto.getSadrzaj());

        rad.setNaucnaOblast(naucnaOblast);
        rad.setIzdanje(izdanje);
        rad.setAutor(autor);
        rad.setRecenzenti(new ArrayList<>());

        rad = radRepository.save(rad);

        for (Korisnik rec:recenzenti) {
            rad.getRecenzenti().add(rec);
            rec.getRadoviRecenzent().add(rad);

            radRepository.save(rad);
            korisnikRepository.save(rec);
        }

        izdanje.getRadovi().add(rad);
        izdanjeRepository.save(izdanje);

        autor.getRadovi().add(rad);
        korisnikRepository.save(autor);

        naucnaOblast.getRadovi().add(rad);
        naucnaOblastRepository.save(naucnaOblast);


        RadElastic radElastic = new RadElastic();

        radElastic.setId(rad.getId().toString());
        radElastic.setNaucnaOblast(naucnaOblast.getNaziv());
        radElastic.setAutor(rad.getAutor().getIme()+" "+rad.getAutor().getPrezime());
        radElastic.setKljucneReci(rad.getKljucneReci());
        radElastic.setNazivCasopisa(izdanje.getCasopis().getNaziv());
        radElastic.setNaslov(rad.getNaslov());
        radElastic.setApstrakt(rad.getApstrakt());


        try {
            File f = storageService.loadAsResource("/home/milan/Desktop/Projects/KP/naucna-centrala/webshop/src/main/resources/pdf/"+rad.getSadrzaj()).getFile();

            PDDocument pdDoc = PDDocument.load(f);
            String parsedText = new PDFTextStripper().getText(pdDoc);

            parsedText = parsedText.replace("\n"," ");

            radElastic.setSadrzaj(parsedText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        radElasticRepository.save(radElastic);

        return rad.getId();
    }

    @Override
    public List<RadFoundDTO> simpleQuery(ElementDTO element){
        List<RadFoundDTO> ret = new ArrayList<>();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        Iterable<RadElastic> radovi = new ArrayList<>();

        BoolQueryBuilder query = QueryBuilders.boolQuery();


        if(element.getValue() != null && element.getValue().charAt(0) == '"' && element.getValue().charAt(element.getValue().length()-1) == '"'){

            System.out.println("PHRASE");

            element.setValue(element.getValue().substring(1, element.getValue().length()-1));

            query = QueryBuilders.boolQuery();
            query.must(matchPhraseQuery(element.getField(),element.getValue()).analyzer("serbian"));
            highlightBuilder.field(element.getField());

            radovi = radElasticRepository.search(query);
        }else{

            System.out.println("SIMPLE");

            query = QueryBuilders.boolQuery();
            query.must(matchQuery(element.getField(),element.getValue()).analyzer("serbian"));
            highlightBuilder.field(element.getField());

            radovi = radElasticRepository.search(query);
        }

        for (RadElastic rad:radovi) {
            RadFoundDTO dto = new RadFoundDTO();

            dto.setNaslov(rad.getNaslov());
            dto.setApstrakt(rad.getApstrakt());
            dto.setKljucneReci(rad.getKljucneReci());
            dto.setNaucnaOblast(rad.getNaucnaOblast());
            dto.setSadrzaj(rad.getSadrzaj());
            dto.setAutor(rad.getAutor());
            dto.setId(rad.getId());
            dto.setNazivCasopisa(rad.getNazivCasopisa());

            ret.add(dto);
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withHighlightBuilder(highlightBuilder)
                .build();

        SearchHits<RadElastic> searchHits = operations.search(searchQuery,
                RadElastic.class,
                IndexCoordinates.of("naucni-radovi"));

        for(SearchHit<RadElastic> hit : searchHits.getSearchHits()) {

            String sazetak = "";

            Map<String, List<String>> highlightMap = hit.getHighlightFields();
            for (String field : highlightMap.keySet()) {
                List<String> highlights = highlightMap.get(field);
                System.out.println("-------------HIGHLIGHTS " + field + "-------------------");
                for (String s : highlights) {
                    System.out.println("-------------HIGHLIGHT: " + s);
                    sazetak = sazetak + "..." + s;
                }
            }
            sazetak = sazetak + "...";

            for(RadFoundDTO radFoundDTO : ret){
                if(hit.getId().equals(radFoundDTO.getId())){

                    sazetak = sazetak.replace("<em>","<b>");
                    sazetak = sazetak.replace("</em>","</b>");

                    radFoundDTO.setSazetak(sazetak);
                    break;
                }
            }
        }

        return ret;
    }


    @Override
    public List<RadFoundDTO> boolQuery(List<ElementDTO> elements){
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        for (ElementDTO element:elements) {
            if(element.getOperator().equals("MUST")){
                if(element.getValue() != null && element.getValue().charAt(0) == '"' && element.getValue().charAt(element.getValue().length()-1) == '"'){
                    System.out.println("PHRASE MUST");

                    element.setValue(element.getValue().substring(1, element.getValue().length() - 1));

                    query.must(matchPhraseQuery(element.getField(),element.getValue()).analyzer("serbian"));
                    highlightBuilder.field(element.getField());

                }else{

                    System.out.println("TERMS MUST");

                    query.must(matchQuery(element.getField(),element.getValue()).operator(Operator.AND).analyzer("serbian"));
                    highlightBuilder.field(element.getField());
                }
            }else if(element.getOperator().equals("SHOULD")){

                if(element.getValue() != null && element.getValue().charAt(0) == '"' && element.getValue().charAt(element.getValue().length()-1) == '"'){
                    System.out.println("PHRASE SHOULD");

                    element.setValue(element.getValue().substring(1, element.getValue().length() - 1));

                    query.should(matchPhraseQuery(element.getField(),element.getValue()).analyzer("serbian"));
                    highlightBuilder.field(element.getField());

                }else{
                    System.out.println("TERMS SHOULD");

                    query.should(matchQuery(element.getField(),element.getValue()).operator(Operator.AND).analyzer("serbian"));
                    highlightBuilder.field(element.getField());
                }
            }
        }

        Iterable<RadElastic> radovi = radElasticRepository.search(query);


        List<RadFoundDTO> ret = new ArrayList<>();

        for (RadElastic rad:radovi) {
            RadFoundDTO dto = new RadFoundDTO();

            dto.setNaslov(rad.getNaslov());
            dto.setApstrakt(rad.getApstrakt());
            dto.setKljucneReci(rad.getKljucneReci());
            dto.setNaucnaOblast(rad.getNaucnaOblast());
            dto.setSadrzaj(rad.getSadrzaj());
            dto.setAutor(rad.getAutor());
            dto.setId(rad.getId());
            dto.setNazivCasopisa(rad.getNazivCasopisa());

            ret.add(dto);
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withHighlightBuilder(highlightBuilder)
                .build();

        SearchHits<RadElastic> searchHits = operations.search(searchQuery,
                RadElastic.class,
                IndexCoordinates.of("naucni-radovi"));

        for(SearchHit<RadElastic> hit : searchHits.getSearchHits()){

            String sazetak = "";

            Map<String, List<String>> highlightMap = hit.getHighlightFields();
            for(String field : highlightMap.keySet()) {
                List<String> highlights = highlightMap.get(field);
                System.out.println("-------------HIGHLIGHTS " + field + "-------------------");
                for (String s : highlights) {
                    System.out.println("-------------HIGHLIGHT: " + s);
                    sazetak = sazetak + "..." +s;
                }
            }
            sazetak = sazetak+"...";

            for(RadFoundDTO radFoundDTO : ret){
                if(hit.getId().equals(radFoundDTO.getId())){

                    sazetak = sazetak.replace("<em>","<b>");
                    sazetak = sazetak.replace("</em>","</b>");

                    radFoundDTO.setSazetak(sazetak);
                    break;
                }
            }
        }

        return ret;
    }

}
