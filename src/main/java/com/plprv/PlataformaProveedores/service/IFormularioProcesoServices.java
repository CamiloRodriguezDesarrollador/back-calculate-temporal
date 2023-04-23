package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.FormularioProceso;

import java.util.List;

public interface IFormularioProcesoServices {

    public List<FormularioProceso> encontrarFormularioProceso();

    public FormularioProceso encontrarFormularioPorParametros(Integer forId,Integer fodId,Integer proId,Integer sprId,Integer perId,Integer crtId,String tdcTd,Integer idEmppal);
    public void crearFormularioProceso(FormularioProceso formularioProceso);

    public FormularioProceso cambiarEstadoFormularioProceso(FormularioProceso formularioProceso);

    public List<FormularioProceso> encontrarFormulariosPorPeriodo(Integer perId);

    public FormularioProceso encontrarFormulariosPorId(Integer fopId);

    public List<FormularioProceso> encontrarFormulariosPorDetalle(Integer fodId);

}
