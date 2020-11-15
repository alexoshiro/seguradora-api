package project.alexoshiro.seguradora.migration.changelogs;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Profile;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.javafaker.Faker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import project.alexoshiro.seguradora.migration.CpfGenerator;

@Profile("test")
@ChangeLog
public class TestPopulateMock {

	@ChangeSet(order = "003", id = "1605397192", author = "Alex")
	public void createTestDataInDatabase(MongoDatabase db) {
		MongoCollection<Document> clientCollection = db.getCollection("clients");

		Faker faker = new Faker(new Locale("pt", "BR"));

		Document document = new Document("_id", new ObjectId("5e32fbb40d71210d2c4c2ab5"))
				.append("fullName", "Teste")
				.append("cpf", CpfGenerator.generateCPF())
				.append("city", faker.address().city())
				.append("uf", faker.address().stateAbbr());

		clientCollection.insertOne(document);

		MongoCollection<Document> insurancePoliciesCollection = db.getCollection("insurance_policies");

		List<Document> toInsert = new ArrayList<>();
		toInsert.add(new Document("_id", new ObjectId("5faf14b4d391f84bdb7399d3"))
				.append("number", 4816512605616465920l)
				.append("termStart", "2020-11-13T22:48:34.000Z")
				.append("termEnd", LocalDateTime.now(ZoneOffset.UTC).plusDays(4))
				.append("licensePlate", "ABC123")
				.append("value", "40000"));
		toInsert.add(new Document("_id", new ObjectId("5faf14b9d391f84bdb7399d4"))
				.append("number", 7289313636067231744l)
				.append("termStart", "2020-11-13T22:48:34.000Z")
				.append("termEnd", LocalDateTime.now(ZoneOffset.UTC).minusDays(4))
				.append("licensePlate", "ABC122")
				.append("value", "40000"));
		toInsert.add(new Document("_id", new ObjectId("5faf27d30dd6b70d7629220d"))
				.append("number", 5341382864787223552l)
				.append("termStart", "2020-11-13T22:48:34.000Z")
				.append("termEnd", LocalDateTime.now(ZoneOffset.UTC))
				.append("licensePlate", "ABC121")
				.append("value", "40000"));
		insurancePoliciesCollection.insertMany(toInsert);
	}
}
