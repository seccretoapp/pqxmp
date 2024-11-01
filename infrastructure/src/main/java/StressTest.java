import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StressTest {
  private static final int NUM_THREADS = 3000; // Número de usuários simultâneos
  private static final int NUM_REQUESTS = 1000; // Número total de requisições por thread

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
    String[] urls = {
      "http://localhost:8070",  // HTTP/1.1
      "http://localhost:8080"   // HTTP/2
    };

    // Envia requisições simultâneas para cada URL
    for (String url : urls) {
      for (int i = 0; i < NUM_THREADS; i++) {
        executorService.submit(() -> sendRequests(url));
      }
    }

    // Finaliza o pool de threads após a execução de todas as tarefas
    executorService.shutdown();
    try {
      executorService.awaitTermination(10, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      System.err.println("O teste foi interrompido: " + e.getMessage());
    }
    System.out.println("Teste de estresse finalizado.");
  }

  private static void sendRequests(String urlString) {
    int successfulRequests = 0;
    int failedRequests = 0;
    long totalResponseTime = 0;

    for (int i = 0; i < NUM_REQUESTS; i++) {
      try {
        long startTime = System.currentTimeMillis();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        long responseTime = System.currentTimeMillis() - startTime;
        totalResponseTime += responseTime;

        if (responseCode == HttpURLConnection.HTTP_OK) {
          successfulRequests++;
        } else {
          failedRequests++;
        }

        // Opcional: Leitura da resposta para garantir a execução completa
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
          while (reader.readLine() != null) {}
        }

        System.out.printf("Requisição %d: Tempo de resposta = %d ms%n", i + 1, responseTime);

      } catch (Exception e) {
        failedRequests++;
        System.err.println("Erro ao enviar requisição: " + e.getMessage());
      }
    }

    // Resultados do teste para cada thread
    System.out.printf("URL: %s | Sucessos: %d | Falhas: %d | Tempo médio de resposta: %d ms%n",
      urlString, successfulRequests, failedRequests,
      (successfulRequests > 0) ? totalResponseTime / successfulRequests : 0);
  }
}
