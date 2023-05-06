package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.FormularioProceso;

import java.util.List;

public interface IFormularioProcesoServices {

    public List<FormularioProceso> encontrarFormularioProceso(Integer idEmppal);

    public FormularioProceso encontrarFormularioPorParametros(Integer forId,Integer fodId,Integer proId,Integer sprId,
                                                              Integer perId,Integer crtId,String tdcTd,Integer idEmppal);
    public void crearFormularioProceso(FormularioProceso formularioProceso);

    public FormularioProceso cambiarEstadoFormularioProceso(FormularioProceso formularioProceso);

    public List<FormularioProceso> encontrarFormulariosPorPeriodo(Integer perId, Integer idEmppal);

    public FormularioProceso encontrarFormulariosPorId(Integer fopId, Integer idEmppal);

    public List<FormularioProceso> encontrarFormulariosPorDetalle(Integer fodId, Integer idEmppal);

}
