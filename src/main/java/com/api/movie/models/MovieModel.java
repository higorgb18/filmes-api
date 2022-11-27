package com.api.movie.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_MOVIES")
public class MovieModel implements Serializable {
    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String movieName;
    @Column(nullable = false)
    private String movieGenre;
    @Column(nullable = false)
    private LocalDateTime registrationDate;
}
