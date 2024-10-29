package com.seccreto.role;

public enum RoleType {
  ADMIN("ADMIN"),
  USER("USER");
  
  private final String value;
  
  RoleType(final String value){
    this.value = value;
  }
  
  public String getValue(){
    return value;
  }
}
