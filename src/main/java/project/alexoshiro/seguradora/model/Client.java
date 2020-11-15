package project.alexoshiro.seguradora.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.alexoshiro.seguradora.dto.ClientDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clients")
public class Client {

	@Id
	private ObjectId id;

	@NotNull
	private String fullName;

	@NotNull
	@Indexed(unique = true)
	private String cpf;

	@NotNull
	private String city;

	@NotNull
	private String uf;

	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime lastMofifiedDate;

	public ClientDTO convertToDTO() {
		return ClientDTO.builder()
				.id(this.id.toString())
				.fullName(this.fullName)
				.cpf(this.cpf)
				.city(this.city)
				.uf(this.uf)
				.build();
	}
}
