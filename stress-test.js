import http from 'k6/http';
import { check, sleep } from 'k6';

// Configurações de opções
export let options = {
  // Definindo a duração do teste para 20 minutos
  duration: '20m',
  // Número de usuários virtuais
  vus: 3000, // Ajuste conforme necessário para o teste
  // Configurando a taxa de erro permitida
  thresholds: {
    'http_req_failed': ['rate<0.002'], // A taxa de erro deve ser menor que 0.2%
  },
  tls: {
    insecure: true, // Ignora a validação do certificado SSL
  },
};

export default function () {
  const res = http.get('https://localhost:8080/'); // Altere para o seu endpoint

  // Verifica se a resposta foi bem-sucedida
  check(res, {
    'is status 200': (r) => r.status === 200,
  });

  sleep(1); // espera 1 segundo entre as requisições
}