package com.seccreto.message;

public enum MessageType {
  TEXT("Text"),
  IMAGE("Image"),
  VIDEO("Video"),
  AUDIO("Audio"),
  FILE("File");
  
  private final String value;
  
  MessageType(final String value){
    this.value = value;
  }
  
  public String getValue(){
    return value;
  }
}
