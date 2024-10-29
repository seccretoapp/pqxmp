package com.seccreto.status;

public enum SystemStatusType {
  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE"),
  MAINTENANCE("MAINTENANCE");
  
  private final String value;
  
  SystemStatusType(final String value){
    this.value = value;
  }
  
  public String getValue(){
    return value;
  }
}
