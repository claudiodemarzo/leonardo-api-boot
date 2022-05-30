package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.UtentiPreferences;

import java.util.Optional;

public interface UtentiPreferencesService {

	Optional<UtentiPreferences> getById(Integer id);

	UtentiPreferences save(UtentiPreferences up);
}
