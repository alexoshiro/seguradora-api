package project.alexoshiro.seguradora.dto;

import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.alexoshiro.seguradora.model.Client;
import project.alexoshiro.seguradora.util.MessageUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClientDTO {

	private String id;

	@NotBlank(message = MessageUtils.NAME_CANNOT_BE_BLANK)
	private String fullName;

	@NotBlank(message = MessageUtils.CPF_CANNOT_BE_BLANK)
	private String cpf;

	@NotBlank(message = MessageUtils.CITY_CANNOT_BE_BLANK)
	private String city;

	@NotBlank(message = MessageUtils.UF_CANNOT_BE_BLANK)
	private String uf;

	public Client convertToModel() {
		return Client.builder()
				.id(this.id != null ? new ObjectId(this.id) : null)
				.fullName(this.fullName)
				.cpf(this.cpf)
				.city(this.city)
				.uf(this.uf)
				.build();
	}
}
