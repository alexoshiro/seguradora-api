package project.alexoshiro.seguradora.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.alexoshiro.seguradora.constant.Formats;
import project.alexoshiro.seguradora.model.InsurancePolicy;
import project.alexoshiro.seguradora.util.MessageUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InsurancePolicyDTO {

	private String id;
	
	private Long number;

	@NotNull(message = MessageUtils.TERM_START_CANNOT_BE_NULL)
	@JsonFormat(pattern = Formats.DATE_TIME_FORMAT)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime termStart;

	@NotNull(message = MessageUtils.TERM_END_CANNOT_BE_NULL)
	@JsonFormat(pattern = Formats.DATE_TIME_FORMAT)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime termEnd;

	@NotBlank(message = MessageUtils.LICENSE_PLATE_CANNOT_BE_BLANK)
	private String licensePlate;

	@Positive
	private Double value;

	public InsurancePolicy convertToModel() {
		return InsurancePolicy.builder()
				.id(this.id != null ? new ObjectId(this.id) : null)
				.number(this.number != null ? this.number : 0)
				.termStart(this.termStart)
				.termEnd(this.termEnd)
				.licensePlate(this.licensePlate)
				.value(this.value)
				.build();
	}
}
