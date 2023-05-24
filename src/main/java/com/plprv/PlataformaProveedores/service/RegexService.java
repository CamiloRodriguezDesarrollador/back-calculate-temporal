package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IFormularioDao;
import com.plprv.PlataformaProveedores.entity.Formulario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegexService implements IRegexService {

    @Override
    public boolean isTextNormal(String texto) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()¿=:/\\-]{2,300}$");
        return pattern.matcher(texto).matches();
    }

    @Override
    public boolean isObservacion(String texto) {
         Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()¿=:/\\-]{0,300}$");

        return pattern.matcher(texto).matches();
    }
    @Override
    public boolean isData(String texto) {
         Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ./\\-]{0,300}$");

        return pattern.matcher(texto).matches();
    }

    @Override
    public boolean isSelectText(String texto) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.?()¿=:/\\-]{1,50}$");
        return pattern.matcher(texto).matches();
    }

    @Override
    public boolean isPassword(String texto) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.,@_#$%&//()=?¡!/-]{6,300}$");
        return pattern.matcher(texto).matches();
    }

    @Override
    public boolean isSelectNumber(Integer texto) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ./\\-]{1,50}$");
        return pattern.matcher(texto.toString()).matches();
    }

    @Override
    public boolean isNumber(Integer texto) {
        Pattern pattern = Pattern.compile("^\\d{6,30}$");
        return pattern.matcher(texto.toString()).matches();
    }

    @Override
    public boolean isId(Integer texto) {
        Pattern pattern = Pattern.compile("^\\d{1,30}$");
        return pattern.matcher(texto.toString()).matches();
    }

    @Override
    public boolean isDate(String texto) {
        Pattern pattern = Pattern.compile("^\\d{4}([\\-/.])(0?[1-9]|1[1-2])\\1(3[01]|[12][0-9]|0?[1-9])$");
        return pattern.matcher(texto).matches();
    }

    @Override
    public boolean isMail(String texto) {
        Pattern pattern = Pattern.compile("\\S+@\\S+\\.\\S+");
        return pattern.matcher(texto).matches();
    }
}
