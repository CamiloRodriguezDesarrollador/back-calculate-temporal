package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFormularioProcesoDao extends CrudRepository<FormularioProceso, Long> {

    public FormularioProceso findByForIdAndFodIdAndProIdAndSprIdAndPerIdAndCrtIdAndTdcTdAndIdEmppal(Integer forId, Integer fodId, Integer proId, Integer sprId, Integer perId ,
                                                                                                    Integer crtId, String tdcTd , Integer idEmppal);

    public List<FormularioProceso> findByPerIdAndIdEmppal(Integer perId, Integer IdEmppal);

    public FormularioProceso findByFopIdAndIdEmppal(Integer fopId, Integer idEmppal);

    public List<FormularioProceso> findByFodIdAndIdEmppal(Integer fodId, Integer idEmppal);

    @Query("SELECT p FROM FormularioProceso p WHERE p.idEmppal = :idEmppal ORDER BY p.fodId")
    public List<FormularioProceso> findAllByIdEmppalOrderByFodId(Integer idEmppal);

}
