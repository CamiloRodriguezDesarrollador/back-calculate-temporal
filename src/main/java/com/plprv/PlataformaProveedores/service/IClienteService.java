package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Cliente;
import java.util.List;

public interface IClienteService {

    public List<Cliente> encontrarClientes(String nitEstado);

    public Cliente encontrarClientePorId(Integer idEmppal);

    public Cliente actualizarCliente (Cliente cliente);

    public void borrarCliente (Long idEmppal);




}
