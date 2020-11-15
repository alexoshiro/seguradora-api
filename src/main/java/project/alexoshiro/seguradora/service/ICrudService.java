package project.alexoshiro.seguradora.service;

import java.util.Optional;

import project.alexoshiro.seguradora.dto.PaginatedResultDTO;

public interface ICrudService<T> {

	PaginatedResultDTO<T> find(String baseUrl, Integer page, Integer pageItems);

	Optional<T> save(T obj);

	Optional<T> update(String id, T obj);

	Optional<T> findById(String id);

	Optional<T> deleteById(String id);
}
