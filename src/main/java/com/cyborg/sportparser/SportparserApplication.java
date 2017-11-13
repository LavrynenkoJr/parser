package com.cyborg.sportparser;

import com.cyborg.sportparser.parser.BCAAParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportparserApplication implements CommandLineRunner {

	private final BCAAParser bcaaParser;

	@Autowired
	public SportparserApplication(BCAAParser bcaaParser) {
		this.bcaaParser = bcaaParser;
	}

	public static void main(String[] args) {
		SpringApplication.run(SportparserApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {

		bcaaParser.parserTest();

	}
}
