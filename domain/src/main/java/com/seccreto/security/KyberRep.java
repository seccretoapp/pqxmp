package com.seccreto.security;

import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class KyberRep implements Serializable {
  @java.io.Serial
  private static final long serialVersionUID = -4757683898830641853L;
  
  public enum Type {
    KYBER_PUBLIC_KEY
  }
  
  private static final String Kyber512 = "Kyber512";
  private static final String Kyber768 = "Kyber768";
  private static final String Kyber1024 = "Kyber1024";
  
  private final Type type;
  
  private final String algorithm;
  
  private final byte[] encoded;
  
  public KyberRep(Type type, String algorithm, byte[] encoded) {
    
    if (type == null || algorithm == null || encoded == null) {
      throw new NullPointerException("All parameters should not be null");
    }
    
    this.type = type;
    this.algorithm = algorithm;
    this.encoded = encoded;
  }
  
  protected Object readResolve() throws ObjectStreamException {
    try {
      if(type == Type.KYBER_PUBLIC_KEY) {
        return new KyberFactory(encoded);
      } else {
        throw new NotSerializableException
          ("unrecognized type: " + type);
      }
    }catch (NotSerializableException nse) {
      throw nse;
    } catch (Exception e) {
      NotSerializableException nse = new NotSerializableException
        ("java.security.Key: " +
          "[" + type + "] " +
          "[" + algorithm + "] ");
      nse.initCause(e);
      throw nse;
    }
  }
}
