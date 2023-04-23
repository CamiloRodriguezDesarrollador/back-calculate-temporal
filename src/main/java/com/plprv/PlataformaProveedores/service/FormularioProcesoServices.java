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
    public List<FormularioProceso> encontrarFormularioProceso() {
        return (List<FormularioProceso>) formularioProcesoDao.findAll();
    }

    @Override
    public FormularioProceso encontrarFormularioPorParametros(Integer forId,Integer fodId,Integer proId,Integer sprId,Integer perId,Integer crtId,String tdcTd,Integer idEmppal) {
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
    public List<FormularioProceso> encontrarFormulariosPorPeriodo(Integer perId) {
        return (List<FormularioProceso>) formularioProcesoDao.findByPerId(perId);
    }

    @Override
    public FormularioProceso encontrarFormulariosPorId(Integer fopId) {
        return (FormularioProceso) formularioProcesoDao.findByFopId(fopId);
    }

    @Override
    public List<FormularioProceso> encontrarFormulariosPorDetalle(Integer fodId) {
        return (List<FormularioProceso>) formularioProcesoDao.findByFodId(fodId);
    }
}
