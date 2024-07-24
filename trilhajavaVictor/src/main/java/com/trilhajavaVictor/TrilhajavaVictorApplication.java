package com.trilhajavaVictor;

import com.trilhajavaVictor.model.DadosSerie;
import com.trilhajavaVictor.service.ConsumoApi;
import com.trilhajavaVictor.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrilhajavaVictorApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TrilhajavaVictorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=9d1fced8");
	//	System.out.println(json);
		//json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		var converteDados = new ConverteDados();
		DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dados);

	}
}
