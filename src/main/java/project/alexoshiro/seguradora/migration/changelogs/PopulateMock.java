package project.alexoshiro.seguradora.migration.changelogs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.bson.Document;
import org.springframework.context.annotation.Profile;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.javafaker.Faker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import project.alexoshiro.seguradora.migration.CpfGenerator;

@Profile({ "dev", "test" })
@ChangeLog
public class PopulateMock {

	@ChangeSet(order = "001", id = "1605396693", author = "Alex")
	public void insertClientsInDatabase(MongoDatabase db) {
		MongoCollection<Document> clientCollection = db.getCollection("clients");
		List<Document> clients = new ArrayList<>();
		Faker faker = new Faker(new Locale("pt", "BR"));

		for (int i = 0; i < 1000; i++) {
			clients.add(
					new Document("fullName", faker.name().fullName())
							.append("cpf", CpfGenerator.generateCPF())
							.append("city", faker.address().city())
							.append("uf", faker.address().stateAbbr()));
		}
		clientCollection.insertMany(clients);
	}

	@ChangeSet(order = "002", id = "1605396702", author = "Alex")
	public void insertInsurancePoliciesInDatabase(MongoDatabase db) {
		MongoCollection<Document> policiesCollection = db.getCollection("insurance_policies");
		List<Document> policies = new ArrayList<>();
		int mockSize = 500;
		List<Long> numbers = generateUniquieRandomInsuranceNumber(mockSize);
		var now = LocalDateTime.now();
		var r = new Random();
		for (int i = 0; i < mockSize; i++) {
			policies.add(
					new Document("number", numbers.get(i))
							.append("termStart", i % 2 == 0 ? now.plusDays(i + 1l)
									: now.minusDays((i + 1) * 2l))
							.append("termEnd", i % 2 == 0 ? now.plusDays((i + 1) * 2l)
									: now.minusDays(i + 2l))
							.append("licensePlate", "ABCD" + (1000 + i))
							.append("value", randomValue(r)));
		}
		policiesCollection.insertMany(policies);
	}

	private List<Long> generateUniquieRandomInsuranceNumber(int mockSize) {
		List<Long> numbers = new ArrayList<>();
		long min = 0l;
		long max = Long.MAX_VALUE;
		while (numbers.size() < mockSize) {
			long generatedLong = min + (long) (Math.random() * (max - min));
			if (!numbers.contains(generatedLong)) {
				numbers.add(generatedLong);
			}
		}

		return numbers;
	}

	private double randomValue(Random r) {
		var min = 500;
		var max = 1000000;
		var value = r.nextInt((max - min) + 1) + min;

		return (double) value;
	}
}
