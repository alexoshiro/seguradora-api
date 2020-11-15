package project.alexoshiro.seguradora.util;

import java.util.ArrayList;
import java.util.List;

import project.alexoshiro.seguradora.dto.ClientDTO;

public class ValidationUtils {

	private static final String CPF_PATTERN = "([0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{2})";

	public static List<String> validateClientRequest(ClientDTO client) {
		List<String> errors = new ArrayList<>();

		if (client.getCpf() != null) {
			if (!client.getCpf().matches(CPF_PATTERN)) {
				errors.add("Formato do CPF inválido");
			} else {
				String cpfWithoutMask = client.getCpf().replace(".", "").replace("-", "");
				if (!CpfUtils.isValidCpf(cpfWithoutMask)) {
					errors.add("CPF inválido.");
				}
			}
		}

		return errors;
	}
}
