package com.seccreto.notification;

import com.seccreto.account.AccountID;
import com.seccreto.message.MessageID;

import java.util.LinkedHashSet;

public interface NotificationGateway {
  // Envia uma notificação para um usuário
  boolean sendNotification(AccountID recipientId, MessageID message);

  // Envia uma notificação para múltiplos usuários de um grupo
  boolean sendNotificationToMultiple(LinkedHashSet<AccountID> recipientIds, MessageID message);

  // Lista todas as notificações recentes para um usuário específico
  LinkedHashSet<NotificationID> getNotifications(AccountID recipientId);
}
