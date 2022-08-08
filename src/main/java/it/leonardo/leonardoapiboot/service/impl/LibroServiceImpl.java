package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.repository.AnnunciLibriRepository;
import it.leonardo.leonardoapiboot.repository.LibriRepository;
import it.leonardo.leonardoapiboot.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibriRepository repo;

    @Autowired
    private AnnunciLibriRepository alrepo;

    @Override
    public List<Libro> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        return repo.findById(isbn);
    }

    @Override
    public List<Libro> findByExactNome(String nome) {
        return repo.findAllByNome(nome);
    }

    @Override
    public List<Libro> findByLikeNome(String nome) {
        return repo.findAllByNomeLikeIgnoreCase("%"+nome+"%");
    }

    @Override
    public List<Libro> findByExactCasaed(String casaed) {
        return repo.findAllByCasaed(casaed);
    }

    @Override
    public List<Libro> findByLikeCasaed(String casaed) {
        return repo.findAllByCasaedLikeIgnoreCase("%"+casaed+"%");
    }

    @Override
    public List<Libro> findByExactAutore(String autore) {
        return repo.findAllByAutori(autore);
    }

    @Override
    public List<Libro> findByLikeAutore(String autore) {
        return repo.findAllByAutoriLikeIgnoreCase("%"+autore+"%");
    }

    @Override
    public List<Libro> findByExactCategoria(String categoria) {
        return repo.findAllByCategoria(categoria);
    }

    @Override
    public List<Libro> findByLikeCategoria(String categoria) {
        return repo.findAllByCategoriaLikeIgnoreCase("%"+categoria+"%");
    }

    @Override
    public List<Libro> findByLikeIsbn(String isbn) {
        return repo.findAllByIsbnLikeIgnoreCase("%"+isbn+"%");
    }

    @Override
    public Libro save(Libro l) {
        return repo.save(l);
    }

    @Override
    public List<Libro> findFirstLimit(int limit) {
        List <Libro> lst = new ArrayList<>(repo.findAll(PageRequest.of(0, limit + 100, Sort.Direction.DESC, "libroId")).toList());
        lst.removeIf(libro -> libro.getAnnunci().isEmpty());
        if(lst.size() > limit)lst.subList(limit, lst.size()).clear();
        return lst;
    }

}
