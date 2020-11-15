package project.alexoshiro.seguradora.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.alexoshiro.seguradora.dto.InsurancePolicyDTO;
import project.alexoshiro.seguradora.dto.InsurancePolicyStatusDTO;
import project.alexoshiro.seguradora.dto.LinkDTO;
import project.alexoshiro.seguradora.dto.PaginatedResultDTO;
import project.alexoshiro.seguradora.model.InsurancePolicy;
import project.alexoshiro.seguradora.repository.InsurancePolicyRepository;
import project.alexoshiro.seguradora.service.ICrudService;
import project.alexoshiro.seguradora.util.CopyUtils;

@Service
public class InsurancePolicyService implements ICrudService<InsurancePolicyDTO> {
	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	public PaginatedResultDTO<InsurancePolicyDTO> find(String baseUrl, Integer page, Integer pageItems) {
		int databasePage = page - 1;
		if (databasePage < 0) {
			databasePage = 0;
		}
		long total = insurancePolicyRepository.count();
		PageRequest pageRequest = PageRequest.of(databasePage, pageItems);
		Page<InsurancePolicy> insurancePolicies = insurancePolicyRepository.findAll(pageRequest);

		List<InsurancePolicyDTO> resultList = new ArrayList<>();
		insurancePolicies.getContent().forEach(insurancePolicy -> resultList.add(insurancePolicy.convertToDTO()));

		return new PaginatedResultDTO<>(resultList,
				new LinkDTO(baseUrl, resultList.size(), insurancePolicies.getTotalPages(), page, pageItems, total));
	}

	public Optional<InsurancePolicyDTO> save(InsurancePolicyDTO insurancePolicy) {
		var model = insurancePolicy.convertToModel();
		model.setNumber(generateUniquieRandomInsuranceNumber());
		InsurancePolicy storedInsurancePolicy = insurancePolicyRepository.save(model);

		return Optional.of(storedInsurancePolicy.convertToDTO());
	}

	public Optional<InsurancePolicyDTO> update(String id, InsurancePolicyDTO insurancePolicy) {
		insurancePolicy.setNumber(null);
		Optional<InsurancePolicyDTO> storedInsurancePolicy = findById(id);
		if (storedInsurancePolicy.isPresent()) {
			InsurancePolicy toSave = storedInsurancePolicy.get().convertToModel();

			CopyUtils.copyNonNullProperties(insurancePolicy, toSave);

			InsurancePolicy updatedInsurancePolicy = insurancePolicyRepository.save(toSave);
			return Optional.of(updatedInsurancePolicy).map(InsurancePolicy::convertToDTO);
		}
		return Optional.empty();
	}

	public Optional<InsurancePolicyDTO> findById(String id) {
		return insurancePolicyRepository.findById(id).map(InsurancePolicy::convertToDTO);
	}

	public Optional<InsurancePolicyDTO> deleteById(String id) {
		insurancePolicyRepository.deleteById(id);
		return findById(id);
	}

	private long generateUniquieRandomInsuranceNumber() {
		long min = 0l;
		long max = Long.MAX_VALUE;
		long generatedLong = min + (long) (Math.random() * (max - min));
		if (findByNumber(generatedLong).isPresent()) {
			return generateUniquieRandomInsuranceNumber();
		}

		return generatedLong;
	}

	public Optional<InsurancePolicyStatusDTO> getInsurancePolicyStatus(long number) {
		var insurancePolicyOptional = insurancePolicyRepository.findByNumber(number);
		if (insurancePolicyOptional.isPresent()) {
			var insurancePolicy = insurancePolicyOptional.get();

			return Optional.of(buildInsurancePolicyStatus(insurancePolicy));
		}
		return Optional.empty();
	}

	public Optional<InsurancePolicyDTO> findByNumber(long number) {
		return insurancePolicyRepository.findByNumber(number).map(InsurancePolicy::convertToDTO);
	}

	private boolean verifyInsurancePolicyExpired(LocalDateTime termEnd) {
		var now = LocalDateTime.now(ZoneOffset.UTC);
		return termEnd.isBefore(now);
	}

	private Long calculateRemaingDays(LocalDateTime termEnd) {
		var now = LocalDateTime.now(ZoneOffset.UTC);
		if (verifyInsurancePolicyExpired(termEnd)) {
			return 0l;
		}
		return ChronoUnit.DAYS.between(now, termEnd);
	}

	private Long calculateExpiredDays(LocalDateTime termEnd) {
		var now = LocalDateTime.now(ZoneOffset.UTC);
		if (verifyInsurancePolicyExpired(termEnd)) {
			return ChronoUnit.DAYS.between(termEnd, now);
		}
		return 0l;
	}
	
	private InsurancePolicyStatusDTO buildInsurancePolicyStatus(InsurancePolicy insurancePolicy) {
		var termEnd = insurancePolicy.getTermEnd();
		var expired = verifyInsurancePolicyExpired(termEnd);

		return InsurancePolicyStatusDTO.builder()
				.id(insurancePolicy.getId().toString())
				.number(insurancePolicy.getNumber())
				.licensePlate(insurancePolicy.getLicensePlate())
				.value(insurancePolicy.getValue())
				.expired(expired)
				.remainingDays(calculateRemaingDays(termEnd))
				.expiredDays(calculateExpiredDays(termEnd))
				.build();
	}
}
