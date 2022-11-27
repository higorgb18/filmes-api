package com.api.movie.services;

import com.api.movie.models.MovieModel;
import com.api.movie.repositories.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    final MovieRepository movieRepository; //cria um ponto de injeção pra avisar sobre a injeção de dependências (autowired via construtor)

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional //roolback q garante q o processo volte ao normal caso de errado (dados quebrados)
    public MovieModel save(MovieModel movieModel) {
        return movieRepository.save(movieModel);
    }

    public boolean existsByMovieName(String movieName) {
        return movieRepository.existsByMovieName(movieName);
    }

    public Page<MovieModel> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Optional<MovieModel> findById(UUID id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public void delete(MovieModel movieModel) {
        movieRepository.delete(movieModel);
    }
}
