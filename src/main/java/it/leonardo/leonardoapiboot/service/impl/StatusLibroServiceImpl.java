package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.StatusLibro;
import it.leonardo.leonardoapiboot.repository.StatusLibroRepository;
import it.leonardo.leonardoapiboot.service.StatusLibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusLibroServiceImpl implements StatusLibroService {

    @Autowired
    private StatusLibroRepository statusLibroRepository;

    @Override
    public Optional<StatusLibro> getStatus(Integer sottCanc, Integer sottNonCanc, Integer scrittCanc, Integer scrittNonCanc, Integer pagManc, Integer pagRov, Integer pagRovMol, Integer copRov, Integer insManc) {
        return statusLibroRepository.findBySottCancAndSottNonCancAndScrittCancAndScrittNonCancAndPagMancAndPagRovAndPagRovMolAndCopRovAndInsManc(sottCanc, sottNonCanc, scrittCanc, scrittNonCanc, pagManc, pagRov, pagRovMol, copRov, insManc);
    }
}