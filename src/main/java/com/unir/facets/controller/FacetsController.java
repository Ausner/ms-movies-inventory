package com.unir.facets.controller;

import com.unir.facets.model.db.Movie;
import com.unir.facets.model.response.MoviesQueryResponse;
import com.unir.facets.service.FacetsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class FacetsController {

    private final FacetsService service;

    // GET - Obtener todas las películas
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = service.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    // GET - Obtener una película por ID
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        Movie movie = service.getMovieById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear una nueva película
    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = service.createMovie(movie);
        return ResponseEntity.ok(createdMovie);
    }

    // PUT - Actualizar una película existente
    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        Movie updatedMovie = service.updateMovie(id, movie);
        if (updatedMovie != null) {
            return ResponseEntity.ok(updatedMovie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Eliminar una película por ID
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        service.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/facets")
    public ResponseEntity<MoviesQueryResponse> getProducts(
            @RequestParam(required = false) List<String> desc,
            @RequestParam(required = false) List<Integer> ano,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean alquilada,
            @RequestParam(required = false) String img,
            @RequestParam(required = false) Long precioAlquiler,
            @RequestParam(required = false) Long precio,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String alquilerHasta,
            @RequestParam(required = false) String video,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false, defaultValue = "0") String page) {


        MoviesQueryResponse response = service.getProducts(
				desc,
				ano,
				name,
				alquilada,
				img,
                precioAlquiler,
                precio,
                director,
                alquilerHasta,
                video,
                id,
                page);
        return ResponseEntity.ok(response);
    }
}
