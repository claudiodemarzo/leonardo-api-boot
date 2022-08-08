package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import java.util.List;

public interface LibriRepository extends JpaRepository<Libro, String> {
    List<Libro> findAllByNome(String nome);

    List<Libro> findAllByNomeLikeIgnoreCase(String like);

    List<Libro> findAllByCasaed(String casaed);

    List<Libro> findAllByCasaedLikeIgnoreCase(String like);

    List<Libro> findAllByAutori(String autore);

    List<Libro> findAllByAutoriLikeIgnoreCase(String like);

    List<Libro> findAllByCategoria(String categoria);

    List<Libro> findAllByCategoriaLikeIgnoreCase(String like);

    List<Libro> findAllByIsbnLikeIgnoreCase(String isbn);


}
