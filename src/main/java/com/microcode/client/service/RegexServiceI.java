package com.microcode.client.service;


import com.microcode.client.entity.*;

public interface RegexServiceI {

    public boolean isTextNormal (String text);

    public boolean isSelectText (String text);

    public boolean isData (String text);

    public boolean isObservacion (String text);

    public boolean isSelectNumber (Integer text);

    public boolean isPassword (String text);

    public boolean isId (Integer text);

    public boolean isNumber (Integer text);

    public boolean isDate (String text);

    public boolean isMail (String text);

    public boolean isClient (Client cli);
    public boolean isClientPar (Client cli);


}
