package com.unir.facets.model.db;

import com.unir.facets.utils.Consts;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "movies", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Movie {
	
	@Id
	private String id;
	
	@Field(type = FieldType.Text, name = Consts.FIELD_NAME)
	private String name;

	@Field(type = FieldType.Text, name = Consts.FIELD_DESCRIPTION)
	private String description;

	@Field(type = FieldType.Text, name = Consts.FIELD_YEAR)
	private Integer year;
	
	@Field(type = FieldType.Integer, name = Consts.FIELD_IMAGE)
	private String image;

	
	@Field(type = FieldType.Search_As_You_Type, name = Consts.FIELD_ALQUILADA)
	private Boolean alquilada;

	@Field(type = FieldType.Keyword, name = Consts.FIELD_VIDEO)
	private String video;

	@Field(type = FieldType.Keyword, name = Consts.FIELD_DIRECTOR)
	private String director;

	@Field(type = FieldType.Keyword, name = Consts.FIELD_PRICE)
	private Integer price;


}
