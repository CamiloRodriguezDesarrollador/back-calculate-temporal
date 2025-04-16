package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.Action;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class RegexService implements RegexServiceI {

    @Override
    public boolean isTextNormal(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()@,_#¿=:/-]{2,300}$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isObservation(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()@,_#¿=:/-]{0,300}$");
        return pattern.matcher(text).matches();
    }
    @Override
    public boolean isData(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()@,_#¿=:/-]{0,300}$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isSelectText(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()¿=:/\\-]{1,50}$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isPassword(String text) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.,@_#$%&/()=?¡!-]{8,300}$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isSelectNumber(Integer text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ./\\-]{1,50}$");
        return pattern.matcher(text.toString()).matches();
    }

    @Override
    public boolean isNumber(Integer text) {
        Pattern pattern = Pattern.compile("^\\d{3,30}$");
        return pattern.matcher(text.toString()).matches();
    }

    @Override
    public boolean isId(Integer text) {
        Pattern pattern = Pattern.compile("^\\d{1,30}$");
        return pattern.matcher(text.toString()).matches();
    }

    @Override
    public boolean isDigit(Integer text) {
        Pattern pattern = Pattern.compile("^\\d{1,1}$");
        return pattern.matcher(text.toString()).matches();
    }

    @Override
    public boolean isDate(String text) {
        Pattern pattern = Pattern.compile("^\\d{4}([\\-/.])(0?[1-9]|1[0-2])\\1(3[01]|[12][0-9]|0?[1-9])$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isMail(String text) {
        Pattern pattern = Pattern.compile("\\S+@\\S+\\.\\S+");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isActionCorrect(Action act) {
        return isTextNormal(act.getActionType())
            && isNumber(act.getActionQuantity()) && isTextNormal(act.getActionNameFunction());
    }


}

