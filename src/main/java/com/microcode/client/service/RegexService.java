package com.microcode.client.service;

import com.microcode.client.entity.*;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class RegexService implements RegexServiceI {

    @Override
    public boolean isTextNormal(String text) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()@_#¿=,:/-]{2,300}$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isObservacion(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()@,_#¿=:/-]{0,300}$");

        return pattern.matcher(text).matches();
    }
    @Override
    public boolean isData(String text) {
         Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ./\\-]{0,300}$");

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
        Pattern pattern = Pattern.compile("^\\d{6,30}$");
        return pattern.matcher(text.toString()).matches();
    }

    @Override
    public boolean isId(Integer text) {
        Pattern pattern = Pattern.compile("^\\d{1,30}$");
        return pattern.matcher(text.toString()).matches();
    }

    @Override
    public boolean isDate(String text) {
        Pattern pattern = Pattern.compile("^\\d{4}([\\-/.])(0?[1-9]|1[1-2])\\1(3[01]|[12][0-9]|0?[1-9])$");
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean isMail(String text) {
        Pattern pattern = Pattern.compile("\\S+@\\S+\\.\\S+");
        return pattern.matcher(text).matches();
    }


    @Override
    public boolean isClient(Client cli) {
        return this.isTextNormal(cli.getCliName()) && this.isTextNormal(cli.getTdcTd()) && this.isTextNormal(cli.getTdcTdGroup()) &&
               this.isTextNormal(cli.getCliNdGroup()) && this.isTextNormal(cli.getCliPrincipal()) &&
               this.isTextNormal(cli.getCliNd()) && this.isTextNormal(cli.getPaiName()) && this.isTextNormal(cli.getDptName()) &&
               this.isTextNormal(cli.getCiuName()) && this.isTextNormal(cli.getCliAddress()) && this.isTextNormal(cli.getCliCellPhone()) &&
               this.isTextNormal(cli.getCliColorSide()) &&  this.isTextNormal(cli.getCliColorLetter()) && this.isTextNormal(cli.getCliColorBack()) &&
               this.isTextNormal(cli.getCliCode());
    }    
    @Override
    public boolean isClientPar(Client cli) {
        return this.isTextNormal(cli.getPaiName()) && this.isTextNormal(cli.getDptName()) && this.isTextNormal(cli.getCiuName()) &&
               this.isTextNormal(cli.getCliAddress()) && this.isTextNormal(cli.getCliCellPhone()) && this.isTextNormal(cli.getCliColorSide()) &&
               this.isTextNormal(cli.getCliColorLetter()) && this.isTextNormal(cli.getCliColorBack());
    }



}
