package project.alexoshiro.seguradora.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class InsurancePolicyControllerTest {

	private static final String INSURANCE_POLICIES_URL = "/api/v1/insurance-policies";

	@Autowired
	protected MockMvc mockMvc;

	@Test
	void getInsurancePoliciesShouldReturnOkAndPayload() throws Exception {
		mockMvc.perform(get(INSURANCE_POLICIES_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.payload", hasSize(50)));
	}

	@Test
	void getInsurancePoliciesByIdShouldReturnOkWithEntity() throws Exception {
		mockMvc.perform(get(INSURANCE_POLICIES_URL + "/5faf14b4d391f84bdb7399d3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.number", is(4816512605616465920l)));
	}

	@Test
	void getInsurancePoliciesByIdDoesNotExistShouldReturnNoContent() throws Exception {
		mockMvc.perform(get(INSURANCE_POLICIES_URL + "/123"))
				.andExpect(status().isNoContent());
	}

	@Test
	void saveInsurancePolicySuccessfullyShouldReturnIt() throws Exception {
		String json = "{\"term_start\":\"2020-11-13T22:48:34.000Z\",\"term_end\":\"2020-12-13T22:48:34.000Z\",\"license_plate\":\"ABC123\",\"value\":\"2\"}";
		mockMvc.perform(post(INSURANCE_POLICIES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.number", greaterThan(0l)));
	}

	@Test
	void updateInsurancePolicySuccessfullyShouldReturnItWithChangedInformation() throws Exception {
		String newValue = "550000";
		String json = "{\"value\":\"" + newValue + "\"}";
		mockMvc.perform(patch(INSURANCE_POLICIES_URL + "/5faf14b4d391f84bdb7399d3")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.value", is(550000.00d)));
	}

	@Test
	void getInsurancePolicyStatusByNumberShouldReturnExpiredAndExpiredDays() throws Exception {
		mockMvc.perform(get(INSURANCE_POLICIES_URL + "/7289313636067231744/status"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.number", is(7289313636067231744l)))
				.andExpect(jsonPath("$.expired", is(true)))
				.andExpect(jsonPath("$.expired_days", is(4)))
				.andExpect(jsonPath("$.remaining_days", is(0)));
	}

	@Test
	void getInsurancePolicyStatusByNumberShouldReturnNotExpiredAndRemainingDays() throws Exception {
		mockMvc.perform(get(INSURANCE_POLICIES_URL + "/4816512605616465920/status"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.number", is(4816512605616465920l)))
				.andExpect(jsonPath("$.expired", is(false)))
				.andExpect(jsonPath("$.expired_days", is(0)))
				.andExpect(jsonPath("$.remaining_days", is(3)));
	}

	@Test
	void deleteInsurancePolicySuccessfullyShouldReturnNoContent() throws Exception {
		mockMvc.perform(delete(INSURANCE_POLICIES_URL + "/5faf27d30dd6b70d7629220d")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent());
	}

}
