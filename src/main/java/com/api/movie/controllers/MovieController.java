package com.api.movie.controllers;

import com.api.movie.dtos.MovieDto;
import com.api.movie.models.MovieModel;
import com.api.movie.services.MovieService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController //bean do spring de controller pra api rest
@CrossOrigin(origins = "*", maxAge = 3600) //permite acesso de qualquer fonte
@RequestMapping("/movies")
public class MovieController {

    final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Object> saveMovie(@RequestBody @Valid MovieDto movieDto) { //objeto em json. @valid pra fazer as validações do model
        if(movieService.existsByMovieName(movieDto.getMovieName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Filme já cadastrado!");
        }

        var movieModel = new MovieModel(); //MovieModel movieModel = new MovieModel()
        BeanUtils.copyProperties(movieDto, movieModel);
        movieModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.save(movieModel));
    }

    @GetMapping
    public ResponseEntity<Page<MovieModel>> getAllMovies(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneMovie(@PathVariable(value = "id") UUID id) { //<object> pois pode n existir o id procurado
        Optional<MovieModel> movieModelOptional = movieService.findById(id);
        if (!movieModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(movieModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable(value = "id") UUID id){
        Optional<MovieModel> movieModelOptional = movieService.findById(id);
        if (!movieModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado!");
        }
        movieService.delete(movieModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Filme deletado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable(value = "id") UUID id,
                                              @RequestBody @Valid MovieDto movieDto){
        Optional<MovieModel> movieModelOptional = movieService.findById(id);
        if (!movieModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado!");
        }
        var movieModel = new MovieModel(); //cria uma nova instância
        BeanUtils.copyProperties(movieDto, movieModel); //faz a conversão de dto para model
        movieModel.setId(movieModelOptional.get().getId()); //mantém o id
        movieModel.setRegistrationDate(movieModelOptional.get().getRegistrationDate()); //mantém a data de registro
        return ResponseEntity.status(HttpStatus.OK).body(movieService.save(movieModel));
    }

}
