package com.unir.facets.model.response;

import com.unir.facets.model.db.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoviesQueryResponse {

    private List<Movie> employees;
    private Map<String, List<AggregationDetails>> aggs;

}
