package project.alexoshiro.seguradora.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.alexoshiro.seguradora.dto.ClientDTO;
import project.alexoshiro.seguradora.dto.PaginatedResultDTO;
import project.alexoshiro.seguradora.dto.StatusDTO;
import project.alexoshiro.seguradora.service.impl.ClientService;
import project.alexoshiro.seguradora.util.MessageUtils;
import project.alexoshiro.seguradora.util.ValidationUtils;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaginatedResultDTO<ClientDTO>> getClients(
			HttpServletRequest request,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "page_items", defaultValue = "50") Integer pageItems) {

		String baseUrl = request.getRequestURL().toString();

		PaginatedResultDTO<ClientDTO> result = clientService.find(baseUrl, page, pageItems);

		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientDTO> getClientById(@PathVariable String id) {
		Optional<ClientDTO> result = clientService.findById(id);

		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(result.get());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveClient(@RequestBody @Valid ClientDTO client) {
		List<String> errors = ValidationUtils.validateClientRequest(client);
		if (errors.isEmpty()) {

			try {
				Optional<ClientDTO> storedClient = clientService.save(client);

				if (storedClient.isEmpty()) {
					return ResponseEntity.unprocessableEntity().build();
				}
				return ResponseEntity.ok(storedClient.get());
			} catch (DuplicateKeyException e) {
				errors.add(MessageUtils.CPF_ALREADY_REGISTERED);
				StatusDTO dto = new StatusDTO(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
						errors);
				return ResponseEntity.unprocessableEntity().body(dto);
			}
		}
		return ResponseEntity.badRequest()
				.body(new StatusDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), errors));

	}

	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateClient(
			@PathVariable String id,
			@RequestBody ClientDTO client) {
		List<String> errors = ValidationUtils.validateClientRequest(client);

		if (errors.isEmpty()) {
			Optional<ClientDTO> updatedClient = clientService.update(id, client);

			if (updatedClient.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(updatedClient.get());
		}
		return ResponseEntity.badRequest()
				.body(new StatusDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), errors));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable String id) {
		Optional<ClientDTO> result = clientService.deleteById(id);

		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.unprocessableEntity().build();
	}
}
