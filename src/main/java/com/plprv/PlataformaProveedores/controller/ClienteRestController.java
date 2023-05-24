package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Cliente;
import com.plprv.PlataformaProveedores.service.IClienteService;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.ObtenerUsuarioAud;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IRegexService regexService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;

    @GetMapping("/cliente")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?>  obtenerClientes(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<Cliente> clientesDb = clienteService.encontrarClientes("A",miIdEmppal);
        if(clientesDb!=null && !clientesDb.isEmpty()){
            return  new ResponseEntity<>(clientesDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/cliente")
    @PreAuthorize("hasAnyAuthority('superadministrador')")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        Integer idEmppal = jsonNode.get("idEmppal").asInt();
        if(opcion.equals("obtenerPorId")){
            Cliente clientesDb = clienteService.encontrarClientePorId(idEmppal);
            if(clientesDb!=null){
                return  new ResponseEntity<>(clientesDb , HttpStatus.OK);
            }else {
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        }
        return ResponseEntity.ok("OK");
    }
    @PutMapping("/cliente")
    @PreAuthorize("hasAnyAuthority('superadministrador')")
    public ResponseEntity<?> actualizarCliente(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String cliente = objectMapper.writeValueAsString(requestBody.get("cliente"));
        JsonNode jsonNode = objectMapper.readTree(cliente);

        try {
            Integer idEmppal = jsonNode.get("id_emppal").asInt();
            String tdcTd = jsonNode.get("tdc_td").asText().trim();
            String nitNd = jsonNode.get("nit_nd").asText().trim();
            String nitNombre = jsonNode.get("nit_nombre").asText().trim();
//            String nitLogo = jsonNode.get("nit_logo").asText().trim();
            String paiNombre = jsonNode.get("pai_nombre").asText().trim();
            String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
            String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
            String nitDireccion = jsonNode.get("nit_direccion").asText().trim();
            String nitTelefono = jsonNode.get("nit_telefono").asText().trim();
            String nitColorSide = jsonNode.get("nit_colorSide").asText().trim();
            String nitColorLetra = jsonNode.get("nit_colorLetra").asText().trim();
            String nitColorFondo = jsonNode.get("nit_colorFondo").asText().trim();

            if (!regexService.isId(idEmppal) || !regexService.isSelectText(tdcTd) || !regexService.isTextNormal(nitNd) ||
                    !regexService.isTextNormal(nitNombre) || !regexService.isTextNormal(paiNombre) || !regexService.isTextNormal(dptNombre) ||
                     !regexService.isTextNormal(ciuNombre) || !regexService.isTextNormal(nitDireccion) || !regexService.isTextNormal(nitTelefono)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            Cliente clientesDb = clienteService.encontrarClientePorId(idEmppal);
            if(clientesDb != null) {
                clientesDb.setTdctd(tdcTd);
                clientesDb.setNitNd(nitNd);
                clientesDb.setNitNombre(nitNombre);
//                clientesDb.setNitLogo(nitLogo);
                clientesDb.setPaiNombre(paiNombre);
                clientesDb.setDptNombre(dptNombre);
                clientesDb.setCiuNombre(ciuNombre);
                clientesDb.setNitDireccion(nitDireccion);
                clientesDb.setNitTelefono(nitTelefono);
                clientesDb.setNitColorside(nitColorSide);
                clientesDb.setNitColorletra(nitColorLetra);
                clientesDb.setNitColorfondo(nitColorFondo);
                clienteService.actualizarCliente(clientesDb);
                return new ResponseEntity<>(clientesDb, HttpStatus.OK);
            }else {
                String data = "no_encontrado";
                return new ResponseEntity<>( data, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.OK);
        }
    }
}

