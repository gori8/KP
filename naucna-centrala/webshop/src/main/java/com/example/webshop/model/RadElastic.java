package com.example.webshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "naucni-radovi", type = "naucni-radovi")
@Setting(settingPath = "/home/milan/Desktop/Projects/KP/naucna-centrala/webshop/src/main/resources/analyzer.json")
public class RadElastic {

    @Id
    private String id;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String naslov;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String apstrakt;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String kljucneReci;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String sadrzaj;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String naucnaOblast;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String autor;

    @Field(
            type = FieldType.Text,
            index = true,
            searchAnalyzer = "serbian",
            analyzer = "serbian",
            store = true
    )
    private String nazivCasopisa;

}
