package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import com.plprv.PlataformaProveedores.entity.Proceso;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFormularioProcesoDao extends CrudRepository<FormularioProceso, Long> {

    public FormularioProceso findByForIdAndFodIdAndProIdAndSprIdAndPerIdAndCrtIdAndTdcTdAndIdEmppal(Integer forId, Integer fodId, Integer proId, Integer sprId, Integer perId ,
                                                                                                    Integer crtId, String tdcTd , Integer idEmppal);

    public List<FormularioProceso> findByPerId(Integer perId);

    public FormularioProceso findByFopId(Integer fopId);

    public List<FormularioProceso> findByFodId(Integer fodId);

}
