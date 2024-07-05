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

@Document(indexName = "movie", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Movie {

	@Field(type = FieldType.Text)
	private String id;
	
	@Field(type = FieldType.Text, name = Consts.FIELD_NAME)
	private String name;

	@Field(type = FieldType.Text, name = Consts.FIELD_DES)
	private String desc;

	@Field(type = FieldType.Integer, name = Consts.FIELD_IMA)
	private String img;

	@Field(type = FieldType.Boolean, name = Consts.FIELD_ALQUILADA)
	private Boolean alquilada;

	@Field(type = FieldType.Long, name = Consts.FIELD_PRECIO_ALQUILER)
	private Long precioAlquiler;

	@Field(type = FieldType.Long, name = Consts.FIELD_PRECIO)
	private Long precio;

	@Field(type = FieldType.Text, name = Consts.FIELD_DIRECTOR)
	private String director;

	@Field(type = FieldType.Integer, name = Consts.FIELD_ANO)
	private Integer ano;

	@Field(type = FieldType.Text, name = Consts.FIELD_ALQUILER_HASTA)
	private String alquilerHasta;

	@Field(type = FieldType.Text, name = Consts.FIELD_VIDEO)
	private String video;


//	@Field(type = FieldType.Integer, name = Consts.FIELD_ID)
//	private String id;






}
