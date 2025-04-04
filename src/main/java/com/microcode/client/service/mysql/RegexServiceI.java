package com.microcode.client.service.mysql;


import com.microcode.client.entity.mysql.Action;

public interface RegexServiceI {

    public boolean isTextNormal (String text);

    public boolean isSelectText (String text);

    public boolean isData (String text);

    public boolean isObservation(String text);

    public boolean isSelectNumber (Integer text);

    public boolean isPassword (String text);

    public boolean isId (Integer text);

    public boolean isNumber (Integer text);
    public boolean isDigit (Integer text);

    public boolean isDate (String text);

    public boolean isMail (String text);

    public boolean isActionCorrect(Action act);

}
