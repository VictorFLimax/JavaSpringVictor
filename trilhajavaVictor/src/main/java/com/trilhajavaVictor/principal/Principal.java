package com.trilhajavaVictor.principal;

import com.trilhajavaVictor.model.DadosEpisodio;
import com.trilhajavaVictor.model.DadosSerie;
import com.trilhajavaVictor.model.DadosTemporadas;
import com.trilhajavaVictor.model.Episodio;
import com.trilhajavaVictor.service.ConsumoApi;
import com.trilhajavaVictor.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
        private Scanner leitura = new Scanner(System.in);

        private final String ENDERECO = "https://www.omdbapi.com/?t=";

        private final String API_KEY = "&apikey=9d1fced8";

        private ConsumoApi consumo = new ConsumoApi();

        private ConverteDados conversor = new ConverteDados();

        public void exibeMenu() {
            System.out.println("Digite o nome da Serie");
            var nomeSerie = leitura.nextLine();
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            System.out.println(dados);
//
         List<DadosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= dados.totalTemporadas(); i++) {
                json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
                temporadas.add(dadosTemporadas);
            }
         //   temporadas.forEach(System.out::println);

//            for (int i = 0; i < dados.totalTemporadas(); i++) {
//                List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//                for (int j = 0; j < episodiosTemporada.size(); j++) {
//                    System.out.println(episodiosTemporada.get(j).titulo());
//                }
//            }

            temporadas.forEach(t->t.episodios().forEach(e -> System.out.println(e.titulo())));


            List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream())
                    .collect(Collectors.toList());

            System.out.println("Escrevendo top 10");

            dadosEpisodios.stream()
                    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                    .peek(e -> System.out.println("Primeiro filtro(N/A) " + e) )
                    .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                    .peek(e -> System.out.println(" Ordenacao " + e) )
                    .limit(10)
                    .peek(e -> System.out.println(" Limit " + e) )
                    .map(e -> e.titulo().toUpperCase())
                    .peek(e -> System.out.println(" Mapeamento " + e) )
                    .forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(d -> new Episodio(t.numero(), d))
                    ).collect(Collectors.toList());

            episodios.forEach(System.out::println);

            System.out.println("BUSCANDO POR TRECHO ");
            System.out.println("Digite um trecho do titulo do episodio");
            var trechoTitulo = leitura.nextLine();

            Optional<Episodio> episodiobuscado = episodios.stream()
                    .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                    .findFirst();
            if (episodiobuscado.isPresent()){
                System.out.println("Episodio encontrado! ");
                System.out.println("Temporada: " + episodiobuscado.get().getTemporada());
            }else {
                System.out.println(" Episodio nao encontrado");
            }

            System.out.println(episodiobuscado);

              System.out.println("BUSCANDO POR TRECHO ");

            System.out.println("apartir de que ano voce deseja ver os episodios ?");
            var ano = leitura.nextInt();
            leitura.nextLine();

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd//MM/yyyy");

            LocalDate dataBusca = LocalDate.of(ano, 1,1);
            episodios.stream()
                    .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                    .forEach(e -> System.out.println(
                            "Temporada = " + e.getTemporada() +
                                    " Episodiio = "+ e.getTitulo() +
                                    " Data Lancamento = " + e.getDataLancamento().format(formatador)
                    ));


            System.out.println("BUSCANDO AVALIACAO POR TEMPORADA ");
            Map<Integer, Double> avaliacaoTemporada = episodios.stream()
                    .filter( e -> e.getAvaliacao()> 0)
                    .collect(Collectors.groupingBy(Episodio::getTemporada,
                            Collectors.averagingDouble(Episodio::getAvaliacao) ));
            System.out.println(avaliacaoTemporada);

            DoubleSummaryStatistics est = episodios.stream()
                    .filter(e-> e.getAvaliacao() > 0)
                    .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
            System.out.println("Media: "+est.getAverage());
            System.out.println("Melhor Episodio: "+est.getMax());
            System.out.println("Pior Episodio: "+est.getMin());
            System.out.println("Qualidade: "+est.getCount());

       }
}
