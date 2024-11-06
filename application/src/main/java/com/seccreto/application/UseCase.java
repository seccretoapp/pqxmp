package com.seccreto.application;

public abstract class UseCase<IN, OUT> {
  public abstract OUT execute(IN anIN);
}

