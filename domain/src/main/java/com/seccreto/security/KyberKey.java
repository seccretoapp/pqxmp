package com.seccreto.security;

import java.io.Serializable;

public interface KyberKey extends Serializable {
    byte[] getEncoded();
}
