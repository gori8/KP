package com.example.webshop.services;

import com.example.webshop.dto.*;
import com.example.webshop.model.*;
import com.example.webshop.repository.*;
import com.example.webshop.util.storage.StorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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

    @Autowired
    KorisnikElasticRepository korisnikElasticRepository;

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

        Rad rad = new Rad();

        rad.setNaslov(dto.getNaslov());
        rad.setApstrakt(dto.getApstrakt());
        rad.setKljucneReci(dto.getKljucneReci());
        rad.setSadrzaj(dto.getSadrzaj());

        rad.setNaucnaOblast(naucnaOblast);
        rad.setIzdanje(izdanje);
        rad.setAutor(autor);

        rad = radRepository.save(rad);
        radRepository.save(rad);

        izdanje.getRadovi().add(rad);
        izdanjeRepository.save(izdanje);

        autor.getRadovi().add(rad);
        korisnikRepository.save(autor);

        naucnaOblast.getRadovi().add(rad);
        naucnaOblastRepository.save(naucnaOblast);


        /*RadElastic radElastic = new RadElastic();

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

        radElasticRepository.save(radElastic);*/

        return rad.getId();
    }

    @Override
    public Long finsihAddingScientificWork(FinishAddingRadDTO dto){

        Rad rad = radRepository.getOne(dto.getRadId());

        for (Long recId : dto.getRecIds()) {
            Korisnik recenzent = korisnikRepository.getOne(recId);

            rad.getRecenzenti().add(recenzent);
            recenzent.getRadoviRecenzent().add(rad);

            korisnikRepository.save(recenzent);
        }

        radRepository.save(rad);

        RadElastic radElastic = new RadElastic();

        radElastic.setId(rad.getId().toString());
        radElastic.setNaucnaOblast(rad.getNaucnaOblast().getNaziv());
        radElastic.setAutor(rad.getAutor().getIme()+" "+rad.getAutor().getPrezime());
        radElastic.setKljucneReci(rad.getKljucneReci());
        radElastic.setNazivCasopisa(rad.getIzdanje().getCasopis().getNaziv());
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

        return dto.getRadId();
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

            dto.setPath(radRepository.getOne(Long.parseLong(rad.getId())).getSadrzaj());

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

            dto.setPath(radRepository.getOne(Long.parseLong(rad.getId())).getSadrzaj());

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


    @Override
    public List<RecenzentDTO> getRecenzenteForNaucnaOblast(Long id){
        Rad rad = radRepository.getOne(id);

        List<Korisnik> recenzentiCasopisa = rad.getIzdanje().getCasopis().getRecenzenti();

        List<RecenzentDTO> ret = new ArrayList<>();

        for(Korisnik rec : recenzentiCasopisa){
            if(containsNaucnaOblast(rec.getNaucneOblasti(),rad.getNaucnaOblast().getId())){
                RecenzentDTO dto = new RecenzentDTO();

                dto.setId(rec.getId());
                dto.setIme(rec.getIme());
                dto.setPrezime(rec.getPrezime());

                ret.add(dto);
            }
        }

        return ret;
    }

    public boolean containsNaucnaOblast(final List<NaucnaOblast> list, final Long id){
        return list.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
    }

    @Override
    public List<RecenzentDTO> geoSearch(Long radId){
        Rad rad = radRepository.getOne(radId);

        System.out.println("GEO SEARCH");

        Korisnik autor = rad.getAutor();

        GeoDistanceQueryBuilder filter = QueryBuilders
                .geoDistanceQuery("lokacija")
                .point(autor.getLatitude(),autor.getLongitude())
                .distance(100, DistanceUnit.KILOMETERS);



        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.mustNot(filter);
        query.must(matchQuery("tip","recenzent"));


        Iterable<KorisnikElastic> recElasticList = korisnikElasticRepository.search(query);

        List<RecenzentDTO> ret = new ArrayList<>();

        for(KorisnikElastic korisnikElastic : recElasticList){

            System.out.println("----USER FOUND BY GEO SEARCH----");
            System.out.println(korisnikElastic.getIme()+" "+korisnikElastic.getPrezime());
            if(containsRecenzent(rad.getIzdanje().getCasopis().getRecenzenti(),Long.parseLong(korisnikElastic.getId()))){
                RecenzentDTO recenzentDTO = new RecenzentDTO();

                recenzentDTO.setId(Long.parseLong(korisnikElastic.getId()));
                recenzentDTO.setIme(korisnikElastic.getIme());
                recenzentDTO.setPrezime(korisnikElastic.getPrezime());

                ret.add(recenzentDTO);
            }
        }

        return ret;
    }

    public boolean containsRecenzent(final List<Korisnik> list, final Long id){
        return list.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
    }

    @Override
    public List<RecenzentDTO> moreLikeThis(Long radId){

        Rad rad = radRepository.getOne(radId);

        String sadrzaj = "";

        try {
            File f = storageService.loadAsResource("/home/milan/Desktop/Projects/KP/naucna-centrala/webshop/src/main/resources/pdf/"+rad.getSadrzaj()).getFile();

            PDDocument pdDoc = PDDocument.load(f);
            String parsedText = new PDFTextStripper().getText(pdDoc);

            sadrzaj = parsedText.replace("\n"," ");

        } catch (IOException e) {
            e.printStackTrace();
        }


        MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThisQuery(
                new String[]{"naslov","sadrzaj","apstrakt","autor","nazivCasopisa","kljucneReci","naucnaOblast"},
                new String []{
                        rad.getNaslov(),
                        rad.getApstrakt(),
                        rad.getKljucneReci(),
                        rad.getNaucnaOblast().getNaziv(),
                        rad.getAutor().getIme()+ " " +rad.getAutor().getPrezime(),
                        rad.getIzdanje().getCasopis().getNaziv(),
                        sadrzaj
                },
                null)
                .minTermFreq(2)
                .minDocFreq(1);

        Iterable<RadElastic> radElasticList = radElasticRepository.search(moreLikeThisQueryBuilder);

        List<RecenzentDTO> ret = new ArrayList<>();

        for(RadElastic radElastic : radElasticList){

            Rad foundRad = radRepository.getOne(Long.parseLong(radElastic.getId()));

            for (Korisnik rec:foundRad.getRecenzenti()) {
                if(!containsRecenzentDTO(ret,rec.getId())){
                    RecenzentDTO recenzentDTO = new RecenzentDTO();
                    recenzentDTO.setId(rec.getId());
                    recenzentDTO.setIme(rec.getIme());
                    recenzentDTO.setPrezime(rec.getPrezime());

                    ret.add(recenzentDTO);
                }
            }
        }

        return ret;
    }

    public boolean containsRecenzentDTO(final List<RecenzentDTO> list, final Long id){
        return list.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
    }
}
