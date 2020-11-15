package project.alexoshiro.seguradora.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.alexoshiro.seguradora.dto.ClientDTO;
import project.alexoshiro.seguradora.dto.LinkDTO;
import project.alexoshiro.seguradora.dto.PaginatedResultDTO;
import project.alexoshiro.seguradora.model.Client;
import project.alexoshiro.seguradora.repository.ClientRepository;
import project.alexoshiro.seguradora.service.ICrudService;
import project.alexoshiro.seguradora.util.CopyUtils;

@Service
public class ClientService implements ICrudService<ClientDTO> {

	@Autowired
	private ClientRepository clientRepository;

	public PaginatedResultDTO<ClientDTO> find(String baseUrl, Integer page, Integer pageItems) {
		int databasePage = page - 1;
		if (databasePage < 0) {
			databasePage = 0;
		}
		long total = clientRepository.count();
		PageRequest pageRequest = PageRequest.of(databasePage, pageItems);
		Page<Client> clients = clientRepository.findAll(pageRequest);

		List<ClientDTO> resultList = new ArrayList<>();
		clients.getContent().forEach(client -> resultList.add(client.convertToDTO()));

		return new PaginatedResultDTO<>(resultList,
				new LinkDTO(baseUrl, resultList.size(), clients.getTotalPages(), page, pageItems, total));
	}

	public Optional<ClientDTO> save(ClientDTO client) {
		Client storedClient = clientRepository.save(client.convertToModel());

		return Optional.of(storedClient.convertToDTO());
	}

	public Optional<ClientDTO> update(String id, ClientDTO client) {
		Optional<ClientDTO> storedClient = findById(id);
		if (storedClient.isPresent()) {
			Client toSave = storedClient.get().convertToModel();

			CopyUtils.copyNonNullProperties(client, toSave);

			Client updatedClient = clientRepository.save(toSave);
			return Optional.of(updatedClient).map(Client::convertToDTO);
		}
		return Optional.empty();
	}

	public Optional<ClientDTO> findById(String id) {
		return clientRepository.findById(id).map(Client::convertToDTO);
	}

	public Optional<ClientDTO> deleteById(String id) {
		clientRepository.deleteById(id);
		return findById(id);
	}
}
