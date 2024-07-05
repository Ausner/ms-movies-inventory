package com.unir.facets.service;

import com.unir.facets.model.db.Movie;
import com.unir.facets.model.response.MoviesQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.unir.facets.data.DataAccessRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacetsService {

	private final DataAccessRepository repository;

	// Obtener todas las películas
	public List<Movie> getAllMovies() {
		return repository.findAll();
	}

	// Obtener una película por ID
	public Movie getMovieById(String id) {
		Optional<Movie> movie = repository.findById(id);
		return movie.orElse(null);
	}

	// Crear una nueva película
	public Movie createMovie(Movie movie) {
		return repository.save(movie);
	}

	// Actualizar una película existente
	public Movie updateMovie(String id, Movie movie) {
		if (!repository.existsById(id)) {
			return null;
		}
		movie.setId(id); // Aseguramos que el ID sea el mismo que el de la película existente
		return repository.save(movie);
	}

	// Eliminar una película por ID
	public void deleteMovie(String id) {
		repository.deleteById(id);
	}

	public MoviesQueryResponse getProducts(
			 List<String> desc,
			List<Integer> ano,
			String name,
			Boolean alquilada,
			String img,
			 Long precioAlquiler,
			 Long precio,
			String director,
			 String alquilerHasta,
			 String video,
			Integer id,
			String page) {

		return repository.findProducts(
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
	}
}
