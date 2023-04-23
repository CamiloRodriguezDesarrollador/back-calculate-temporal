package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IClienteDao extends CrudRepository<Cliente, Long> {

    public List<Cliente> findByNitEstado(String nitEstado);

    public Cliente findByIdEmppal(Integer idEmppal);


}
