package com.unir.facets.data;

import java.util.List;

import com.unir.facets.model.db.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
	
	List<Movie> findAll();
}
