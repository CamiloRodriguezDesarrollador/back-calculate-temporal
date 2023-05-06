package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.ProveedorSistema;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProveedorSistemaDao extends CrudRepository<ProveedorSistema, Long> {


    public ProveedorSistema findByNitNdAndTdcTd(Integer nitNd, String tdcTd);

    @Query("SELECT pe,p.prvEstado FROM ProveedorSistema p JOIN ProveedorSistemaDetalle pe ON pe.pdmConsecutivo = p.pdcConsec AND pe.secuencia = p.prvActivodad" +
            " WHERE (p.nitNd = :nitNd) AND (p.tdcTd = :tdcTd)")
    public Object cantidadDocumentacionTablaDetalle(Integer nitNd, String tdcTd);

}
