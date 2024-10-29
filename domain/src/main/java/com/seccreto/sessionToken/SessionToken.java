package com.seccreto.sessionToken;

import java.util.Base64;
import com.google.gson.Gson;
import java.time.Instant;
import java.util.UUID;

public class SessionToken {
  private String userId;
  private String publicKey;
  private String dilithiumSignature;
  private String timestamp;
  private String sessionId;

  // Construtor
  public SessionToken(String userId, String publicKey, String dilithiumSignature) {
    this.userId = userId;
    this.publicKey = publicKey;
    this.dilithiumSignature = dilithiumSignature;
    this.timestamp = Instant.now().toString();
    this.sessionId = UUID.randomUUID().toString();
  }

  // Método para gerar o token em formato Base64
  public String generateToken() {
    Gson gson = new Gson();
    String jsonToken = gson.toJson(this);
    return Base64.getEncoder().encodeToString(jsonToken.getBytes());
  }

  // Método para validar o token usando a assinatura Dilithium ou outra lógica
  public static boolean validateToken(String token, String expectedPublicKey) {
    String jsonToken = new String(Base64.getDecoder().decode(token));
    Gson gson = new Gson();
    SessionToken sessionToken = gson.fromJson(jsonToken, SessionToken.class);

    // Verifica a chave pública e outros detalhes conforme necessário
    return sessionToken.publicKey.equals(expectedPublicKey);
  }

  // Getters e Setters (se necessário)
  public String getUserId() { return userId; }
  public String getPublicKey() { return publicKey; }
  public String getDilithiumSignature() { return dilithiumSignature; }
  public String getTimestamp() { return timestamp; }
  public String getSessionId() { return sessionId; }
}