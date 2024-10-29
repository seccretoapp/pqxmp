package com.seccreto.security;

public enum ActionType {
  AUTHENTICATE("Authenticate"),
  Deny("Deny");
  
  private final String value;
  
  ActionType(final String value){
    this.value = value;
  }
  
  public String getValue(){
    return value;
  }
}
