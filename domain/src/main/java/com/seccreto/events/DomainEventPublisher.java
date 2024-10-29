package com.seccreto.events;

@FunctionalInterface
public interface DomainEventPublisher {
  void publishEvent(DomainEvent event);
}