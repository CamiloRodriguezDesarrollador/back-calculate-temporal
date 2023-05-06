package com.plprv.PlataformaProveedores.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.plprv.PlataformaProveedores.dao.IClienteDao;
import com.plprv.PlataformaProveedores.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
public class ObtenerUsuarioAud

{
    public String obtnerUsuarioToken(String token) {
        String[] tokenParts = token.split("\\.");
        String tokenPayload = tokenParts[1];
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedPayload = new String(decoder.decode(tokenPayload));
        JsonObject payloadJson = JsonParser.parseString(decodedPayload).getAsJsonObject();
        String sub = payloadJson.get("sub").getAsString();
        return sub;
    }

    public Integer obtnerIdEmppalToken(String token) {
        String[] tokenParts = token.split("\\.");
        String tokenPayload = tokenParts[1];
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedPayload = new String(decoder.decode(tokenPayload));
        JsonObject payloadJson = JsonParser.parseString(decodedPayload).getAsJsonObject();
        Integer idEmppal = payloadJson.get("idEmppal").getAsInt();
        return idEmppal;
    }
}
