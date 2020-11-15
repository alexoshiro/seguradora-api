package project.alexoshiro.seguradora.controller;

import java.util.ArrayList;
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

import project.alexoshiro.seguradora.dto.InsurancePolicyDTO;
import project.alexoshiro.seguradora.dto.InsurancePolicyStatusDTO;
import project.alexoshiro.seguradora.dto.PaginatedResultDTO;
import project.alexoshiro.seguradora.dto.StatusDTO;
import project.alexoshiro.seguradora.service.impl.InsurancePolicyService;
import project.alexoshiro.seguradora.util.MessageUtils;

@RestController
@RequestMapping("/api/v1/insurance-policies")
public class InsurancePolicyController {

	@Autowired
	private InsurancePolicyService insurancePolicyService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaginatedResultDTO<InsurancePolicyDTO>> getInsurancePolicies(
			HttpServletRequest request,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "page_items", defaultValue = "50") Integer pageItems) {

		String baseUrl = request.getRequestURL().toString();

		PaginatedResultDTO<InsurancePolicyDTO> result = insurancePolicyService.find(baseUrl, page, pageItems);

		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InsurancePolicyDTO> getInsurancePolicyById(@PathVariable String id) {
		Optional<InsurancePolicyDTO> result = insurancePolicyService.findById(id);

		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(result.get());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveInsurancePolicy(@RequestBody @Valid InsurancePolicyDTO insurancePolicy) {
		try {
			Optional<InsurancePolicyDTO> storedInsurancePolicy = insurancePolicyService.save(insurancePolicy);

			if (storedInsurancePolicy.isEmpty()) {
				return ResponseEntity.unprocessableEntity().build();
			}
			return ResponseEntity.ok(storedInsurancePolicy.get());
		} catch (DuplicateKeyException e) {
			List<String> errors = new ArrayList<>();
			errors.add(MessageUtils.INSURANCE_POLICY_ALREADY_REGISTERED);
			StatusDTO dto = new StatusDTO(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
					errors);
			return ResponseEntity.unprocessableEntity().body(dto);
		}

	}

	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateInsurancePolicy(
			@PathVariable String id,
			@RequestBody InsurancePolicyDTO insurancePolicy) {

		Optional<InsurancePolicyDTO> updatedInsurancePolicy = insurancePolicyService.update(id, insurancePolicy);

		if (updatedInsurancePolicy.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(updatedInsurancePolicy.get());

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteInsurancePolicy(@PathVariable String id) {
		Optional<InsurancePolicyDTO> result = insurancePolicyService.deleteById(id);

		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.unprocessableEntity().build();
	}

	@GetMapping(value = "/{number}/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InsurancePolicyStatusDTO> getInsurancePolicyStatusByNumber(@PathVariable long number) {
		Optional<InsurancePolicyStatusDTO> result = insurancePolicyService.getInsurancePolicyStatus(number);

		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(result.get());
	}
}
