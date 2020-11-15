package project.alexoshiro.seguradora.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InsurancePolicyStatusDTO {
	
	private String id;
	
	private Long number;

	private boolean expired;

	private String licensePlate;

	private double value;

	private Long remainingDays;

	private Long expiredDays;
}
