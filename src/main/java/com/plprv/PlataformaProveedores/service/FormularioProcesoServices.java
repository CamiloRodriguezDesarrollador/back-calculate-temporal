package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IFormularioProcesoDao;
import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormularioProcesoServices implements IFormularioProcesoServices {

    @Autowired
    private IFormularioProcesoDao formularioProcesoDao;

    @Override
    public List<FormularioProceso> encontrarFormularioProceso(Integer idEmppal) {
        return (List<FormularioProceso>) formularioProcesoDao.findAllByIdEmppalOrderByFodId(idEmppal);
    }

    @Override
    public FormularioProceso encontrarFormularioPorParametros(Integer forId,Integer fodId,Integer proId,Integer sprId,
                                                              Integer perId,Integer crtId,String tdcTd,Integer idEmppal) {
        return (FormularioProceso) formularioProcesoDao.findByForIdAndFodIdAndProIdAndSprIdAndPerIdAndCrtIdAndTdcTdAndIdEmppal(forId, fodId, proId, sprId, perId, crtId, tdcTd, idEmppal) ;
    }

    @Override
    public void crearFormularioProceso(FormularioProceso formularioProceso) {
        formularioProcesoDao.save(formularioProceso);
    }

    @Override
    public FormularioProceso cambiarEstadoFormularioProceso(FormularioProceso formularioProceso) {
        return (FormularioProceso) formularioProcesoDao.save(formularioProceso) ;

    }

    @Override
    public List<FormularioProceso> encontrarFormulariosPorPeriodo(Integer perId, Integer idEmppal) {
        return (List<FormularioProceso>) formularioProcesoDao.findByPerIdAndIdEmppal(perId, idEmppal);
    }

    @Override
    public FormularioProceso encontrarFormulariosPorId(Integer fopId, Integer idEmppal) {
        return (FormularioProceso) formularioProcesoDao.findByFopIdAndIdEmppal(fopId,idEmppal);
    }

    @Override
    public List<FormularioProceso> encontrarFormulariosPorDetalle(Integer fodId, Integer idEmppal) {
        return (List<FormularioProceso>) formularioProcesoDao.findByFodIdAndIdEmppal(fodId, idEmppal);
    }
}
