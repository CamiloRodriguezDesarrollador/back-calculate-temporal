package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.ProveedorSistema;
import com.plprv.PlataformaProveedores.entity.ProveedorSistemaDetalle;
import org.springframework.data.repository.CrudRepository;

public interface IProveedorSistemaDetalleDao extends CrudRepository<ProveedorSistemaDetalle, Long> {


//    public ProveedorSistemaDetalle findByNitNdAndTdcTd(Integer nitNd, String tdcTd);

}
