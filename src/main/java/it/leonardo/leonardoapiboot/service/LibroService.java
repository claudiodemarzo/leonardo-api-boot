package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {
    List<Libro> findAll();

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByExactNome(String nome);

    List<Libro> findByLikeNome(String nome);

    List<Libro> findByExactCasaed(String casaed);

    List<Libro> findByLikeCasaed(String casaed);

    List<Libro> findByExactAutore(String autore);

    List<Libro> findByLikeAutore(String autore);

    List<Libro> findByExactCategoria(String categoria);

    List<Libro> findByLikeCategoria(String categoria);

    Libro save(Libro l);

    List<Libro> findFirstLimit(int limit);

    List<Libro> findByLikeIsbn(String isbn);
}
