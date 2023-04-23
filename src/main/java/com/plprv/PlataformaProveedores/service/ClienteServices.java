package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IClienteDao;
import com.plprv.PlataformaProveedores.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServices implements IClienteService

{
    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> encontrarClientes(String nitEstado) {
        return (List<Cliente>) clienteDao.findByNitEstado(nitEstado);

    }@Override
    @Transactional(readOnly = true)
    public Cliente encontrarClientePorId(Integer idEmppal) {
        return (Cliente) clienteDao.findByIdEmppal(idEmppal);
    }
    @Override
    public Cliente actualizarCliente(Cliente cliente) {
        return (Cliente) clienteDao.save(cliente);
    }

    @Override
    public void borrarCliente(Long id_emppal) {
    }
}
