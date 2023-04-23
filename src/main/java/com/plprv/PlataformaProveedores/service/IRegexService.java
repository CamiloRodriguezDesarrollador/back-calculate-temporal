package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Cliente;

import java.util.List;

public interface IRegexService {

    public boolean isTextNormal (String texto);

    public boolean isSelectText (String texto);

    public boolean isData (String texto);

    public boolean isObservacion (String texto);

    public boolean isSelectNumber (Integer texto);

    public boolean isPassword (String texto);

    public boolean isId (Integer texto);

    public boolean isNumber (Integer texto);

    public boolean isDate (String texto);

    public boolean isMail (String texto);

}
