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
public class LinkDTO {

	private Integer pageItems;

	private Integer totalPages;

	private Integer pageNumber;

	private String next;

	private String previous;

	private long totalItems;

	public LinkDTO(String baseUrl, int pageItemsQuantity, int totalPages, int actualPage, int pageItems,
			long totalItems) {
		this.pageItems = pageItemsQuantity;
		this.totalPages = totalPages;
		this.pageNumber = actualPage;
		this.next = actualPage < totalPages ? createUrl(baseUrl, actualPage + 1, pageItems) : null;
		this.previous = actualPage > 1 ? createUrl(baseUrl, actualPage - 1, pageItems) : null;
		this.totalItems = totalItems;
	}

	private String createUrl(String baseUrl, int page, int pageItems) {
		StringBuilder query = new StringBuilder(baseUrl + "?");
		query.append("page_items=" + pageItems);
		query.append("&page=" + page);
		return query.toString();
	}
}
