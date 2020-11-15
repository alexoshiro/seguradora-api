package project.alexoshiro.seguradora.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.alexoshiro.seguradora.dto.InsurancePolicyDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "insurance_policies")
public class InsurancePolicy {

	@Id
	private ObjectId id;

	@Indexed(unique = true)
	private long number;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime termStart;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime termEnd;

	private String licensePlate;

	private double value;

	public InsurancePolicyDTO convertToDTO() {
		return InsurancePolicyDTO.builder()
				.id(this.id.toString())
				.number(this.number)
				.termStart(this.termStart)
				.termEnd(this.termEnd)
				.licensePlate(this.licensePlate)
				.value(this.value)
				.build();
	}
}
