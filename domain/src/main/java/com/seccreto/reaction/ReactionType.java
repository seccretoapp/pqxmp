package com.seccreto.reaction;

public enum ReactionType {
  LIKE("Like"),
  EMOJI("Emoji"),
  VOTE("Vote");
  
  private final String value;
  
  ReactionType(final String value){
    this.value = value;
  }
  
  public String getValue(){
    return value;
  }
}
