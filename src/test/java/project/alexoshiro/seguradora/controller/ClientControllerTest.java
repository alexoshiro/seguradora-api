package project.alexoshiro.seguradora.controller;

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
class ClientControllerTest {

	private static final String CLIENTS_URL = "/api/v1/clients";
	
	@Autowired
	protected MockMvc mockMvc;

	@Test
	void getClientsShouldReturnList() throws Exception {
		mockMvc.perform(get(CLIENTS_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.payload", hasSize(50)));
	}

	@Test
	void getClientsByIdShouldReturnClient() throws Exception {
		mockMvc.perform(get(CLIENTS_URL + "/5e32fbb40d71210d2c4c2ab5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.full_name", is("Teste")));
	}

	@Test
	void getClientsByIdClientDoesNotExistShouldReturnNoContent() throws Exception {
		mockMvc.perform(get(CLIENTS_URL + "/123"))
				.andExpect(status().isNoContent());
	}

	@Test
	void createClientSuccessfullyShouldReturnIt() throws Exception {
		String json = "{\"full_name\":\"Tester\",\"city\":\"São Paulo\",\"uf\":\"SP\",\"cpf\":\"916.954.250-30\"}";
		mockMvc.perform(post(CLIENTS_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.full_name", is("Tester")));
	}

	@Test
	void updateClientSuccessfullyShouldReturnClientWithChangedInformation() throws Exception {
		String newCity = "São Paulo";
		String json = "{\"city\":\"" + newCity + "\"}";
		mockMvc.perform(patch(CLIENTS_URL + "/5e32fbb40d71210d2c4c2ab5")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(newCity)));
	}

	@Test
	void deleteClientSuccessfullyShouldReturnNoContent() throws Exception {
		mockMvc.perform(delete(CLIENTS_URL + "/5e32fbb40d71210d2c4c2ab5")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent());
	}

}
