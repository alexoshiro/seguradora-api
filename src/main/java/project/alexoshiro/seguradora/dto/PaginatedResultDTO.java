package project.alexoshiro.seguradora.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResultDTO<T> {
	private List<T> payload;
	private LinkDTO links;
}
