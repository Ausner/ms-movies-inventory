package com.unir.facets.data;

import com.unir.facets.model.db.Movie;
import com.unir.facets.model.response.AggregationDetails;
import com.unir.facets.model.response.MoviesQueryResponse;
import com.unir.facets.utils.Consts;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    private final ElasticsearchOperations elasticClient;

    // Guardar una película
    public Movie save(Movie movie) {
        return elasticClient.save(movie);
    }

    // Obtener todas las películas
    public List<Movie> findAll() {
        SearchHits<Movie> searchHits = elasticClient.search(Query.findAll(), Movie.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Obtener una película por ID
    public Optional<Movie> findById(String id) {
        return Optional.ofNullable(elasticClient.get(id, Movie.class));
    }

    // Verificar si una película existe por ID
    public boolean existsById(String id) {
        return elasticClient.exists(id, Movie.class);
    }

    // Actualizar una película
    public Movie update(String id, Movie movie) {
        movie.setId(id); // Aseguramos que el ID sea el mismo que el de la película existente
        return elasticClient.save(movie);
    }

    // Eliminar una película por ID
    public void deleteById(String id) {
        elasticClient.delete(id, Movie.class);
    }

    @SneakyThrows
    public MoviesQueryResponse findProducts(
            List<String> description,
            List<Integer> year,
            String name,
            Boolean alquilada,
            String video,
            String image,
            String director,
            Integer price,
            String page) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        // Si el usuario ha seleccionado algun valor relacionado con la descripcion, lo añadimos a la query
        if (description != null && !description.isEmpty()) {
            description.forEach(
                    desc -> querySpec.must(QueryBuilders.termQuery(Consts.FIELD_DESCRIPTION, desc))
            );
        }

        // Si el usuario ha seleccionado algun valor relacionado con el year, lo añadimos a la query
        if (year != null && !year.isEmpty()) {
            year.forEach(
                    y -> querySpec.must(QueryBuilders.termQuery(Consts.FIELD_YEAR, y))
            );
        }

        // Si el usuario ha seleccionado algun valor relacionado con el nombre, lo añadimos a la query
        if (!StringUtils.isEmpty(name)) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_NAME, name));
        }


        // Si el usuario ha seleccionado algun valor relacionado con el alquilada, lo añadimos a la query
        if (alquilada != null) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_ALQUILADA, alquilada));
        }

        // Si el usuario ha seleccionado algun valor relacionado con el video, lo añadimos a la query
        if (!StringUtils.isEmpty(video)) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_VIDEO, video));
        }

        // Si el usuario ha seleccionado algun valor relacionado con el image, lo añadimos a la query
        if (!StringUtils.isEmpty(image)) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_IMAGE, image));
        }

        // Si el usuario ha seleccionado algun valor relacionado con el director, lo añadimos a la query
        if (!StringUtils.isEmpty(director)) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_DIRECTOR, director));
        }

        // Si el usuario ha seleccionado algun valor relacionado con el price, lo añadimos a la query
        if (price != null) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_PRICE, price));
        }


        //Si no se ha seleccionado ningun filtro, se añade un filtro por defecto para que la query no sea vacia
        if(!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Construimos la query
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        //Se establece un maximo de 5 resultados, va acorde con el tamaño de la pagina
        nativeSearchQueryBuilder.withMaxResults(5);

        //Podemos paginar los resultados en base a la pagina que nos llega como parametro
        //El tamaño de la pagina es de 5 elementos (pero el propio llamante puede cambiarlo si se habilita en la API)
        int pageInt = Integer.parseInt(page);
        if (pageInt >= 0) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(pageInt,5));
        }

        //Se construye la query
        Query query = nativeSearchQueryBuilder.build();
        // Se realiza la busqueda
        SearchHits<Movie> result = elasticClient.search(query, Movie.class);
        return new MoviesQueryResponse(getResponseMovies(result), getResponseAggregations(result));
    }

    /**
     * Metodo que convierte los resultados de la busqueda en una lista de empleados.
     * @param result Resultados de la busqueda.
     * @return Lista de empleados.
     */
    private List<Movie> getResponseMovies(SearchHits<Movie> result) {
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    /**
     * Metodo que convierte las agregaciones de la busqueda en una lista de detalles de agregaciones.
     * Se ha de tener en cuenta que el tipo de agregacion puede ser de tipo rango o de tipo termino.
     * @param result Resultados de la busqueda.
     * @return Lista de detalles de agregaciones.
     */
    private Map<String, List<AggregationDetails>> getResponseAggregations(SearchHits<Movie> result) {

        //Mapa de detalles de agregaciones
        Map<String, List<AggregationDetails>> responseAggregations = new HashMap<>();

        //Recorremos las agregaciones
        if (result.hasAggregations()) {
            Map<String, Aggregation> aggs = result.getAggregations().asMap();

            //Recorremos las agregaciones
            aggs.forEach((key, value) -> {

                //Si no existe la clave en el mapa, la creamos
                if(!responseAggregations.containsKey(key)) {
                    responseAggregations.put(key, new LinkedList<>());
                }

                //Si la agregacion es de tipo termino, recorremos los buckets
                if (value instanceof ParsedStringTerms parsedStringTerms) {
                    parsedStringTerms.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKey().toString(), (int) bucket.getDocCount()));
                    });
                }

                //Si la agregacion es de tipo rango, recorremos tambien los buckets
                if (value instanceof ParsedRange parsedRange) {
                    parsedRange.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKeyAsString(), (int) bucket.getDocCount()));
                    });
                }
            });
        }
        return responseAggregations;
    }
}
