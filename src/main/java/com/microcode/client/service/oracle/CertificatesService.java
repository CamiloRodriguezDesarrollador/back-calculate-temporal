package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.CertificateJob;
import com.microcode.client.entity.oracle.CertificatePay;
import com.microcode.client.entity.oracle.LoanPay;
import com.microcode.client.entity.oracle.RegisterPay;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Service
@Repository
@AllArgsConstructor
public class CertificatesService {

    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public CertificateJob getDataCertificatedJob(Long nmcteCodigo, Long nmctoNumero, String vcpromediasueldo, String vcdestinatario) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("rhu.qb_portal_rrhh.pb_certificacion_laboral");

        // Parámetros de entrada
        query.registerStoredProcedureParameter("nmcte_codigo", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("nmcto_numero", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("vcpromediasueldo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("vcdestinatario", String.class, ParameterMode.IN);

        query.setParameter("nmcte_codigo", nmcteCodigo);
        query.setParameter("nmcto_numero", nmctoNumero);
        query.setParameter("vcpromediasueldo", vcpromediasueldo);
        query.setParameter("vcdestinatario", vcdestinatario);

        // Parámetros de salida
        query.registerStoredProcedureParameter("vctexto", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcnomempppal", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcnomempfil", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("nmemp_nd", Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcresponsble", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcrolresponsable", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcarearesponsable", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcdirlogo", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcdirfirma", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcTextoConfirmacion", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcPieInfEmpresa", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vctextolm", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vctextonota", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vctextotitulo", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vctextoatte", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("vcerror", String.class, ParameterMode.OUT);

        query.execute();

        CertificateJob response = new CertificateJob();
        response.setTexto((String) query.getOutputParameterValue("vctexto"));
        response.setNombreEmpresaPrincipal((String) query.getOutputParameterValue("vcnomempppal"));
        response.setNombreEmpresaFilial((String) query.getOutputParameterValue("vcnomempfil"));
        response.setEmpresaNd((Long) query.getOutputParameterValue("nmemp_nd"));
        response.setResponsable((String) query.getOutputParameterValue("vcresponsble"));
        response.setRolResponsable((String) query.getOutputParameterValue("vcrolresponsable"));
        response.setAreaResponsable((String) query.getOutputParameterValue("vcarearesponsable"));
        response.setDirLogo((String) query.getOutputParameterValue("vcdirlogo"));
        response.setDirFirma((String) query.getOutputParameterValue("vcdirfirma"));
        response.setTextoConfirmacion((String) query.getOutputParameterValue("vcTextoConfirmacion"));
        response.setPieInfEmpresa((String) query.getOutputParameterValue("vcPieInfEmpresa"));
        response.setTextoLm((String) query.getOutputParameterValue("vctextolm"));
        response.setTextoNota((String) query.getOutputParameterValue("vctextonota"));
        response.setTextoTitulo((String) query.getOutputParameterValue("vctextotitulo"));
        response.setTextoAtte((String) query.getOutputParameterValue("vctextoatte"));
        response.setError((String) query.getOutputParameterValue("vcerror"));

        return response;
    }

//    public CertificatePay getDataCertificatedPay(Long nmcteCodigo, Long nmctoNumero, String vcRangoFechas, Long nmRadicacion) {
//        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("rhu.qb_portal_rrhh.pb_comprobante_pago");
//
//        // Parámetros de entrada
//        query.registerStoredProcedureParameter("nmcte_codigo", Long.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("nmcto_numero", Long.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("vcrangofechas", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("nmradicacion", Long.class, ParameterMode.IN);
//
//        query.setParameter("nmcte_codigo", nmcteCodigo);
//        query.setParameter("nmcto_numero", nmctoNumero);
//        query.setParameter("vcrangofechas", vcRangoFechas);
//        query.setParameter("nmradicacion", nmRadicacion);
//
//        // Parámetros de salida
//        query.registerStoredProcedureParameter("vcnomempresa", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcnomempresafil", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcperiodonomina", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("nmconsecutivohpr", Long.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcnomempleado", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcepl_identificacion", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("nmsueldo", Double.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcipp", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("nmrtefte", Double.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vccentrocosto", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcnombreciudad", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcmensajebasico", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vccesantias", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcmensaje", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcotromensaje", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcmonto", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vcMensaje2", String.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vtpago", ArrayList.class, ParameterMode.OUT);
//        query.registerStoredProcedureParameter("vprestamos", void.class, ParameterMode.REF_CURSOR);
//        query.registerStoredProcedureParameter("vcerror", String.class, ParameterMode.OUT);
//
//        query.execute();
//
//        CertificatePay cert = new CertificatePay();
//        cert.setNombreEmpresa((String) query.getOutputParameterValue("vcnomempresa"));
//        cert.setNombreEmpresaFilial((String) query.getOutputParameterValue("vcnomempresafil"));
//        cert.setPeriodoNomina((String) query.getOutputParameterValue("vcperiodonomina"));
//        cert.setConsecutivoHpr((Long) query.getOutputParameterValue("nmconsecutivohpr"));
//        cert.setNombreEmpleado((String) query.getOutputParameterValue("vcnomempleado"));
//        cert.setIdentificacionEmpleado((String) query.getOutputParameterValue("vcepl_identificacion"));
//        cert.setSueldo((Double) query.getOutputParameterValue("nmsueldo"));
//        cert.setIpp((String) query.getOutputParameterValue("vcipp"));
//        cert.setRtefte((Double) query.getOutputParameterValue("nmrtefte"));
//        cert.setCentroCosto((String) query.getOutputParameterValue("vccentrocosto"));
//        cert.setNombreCiudad((String) query.getOutputParameterValue("vcnombreciudad"));
//        cert.setMensajeBasico((String) query.getOutputParameterValue("vcmensajebasico"));
//        cert.setCesantias((String) query.getOutputParameterValue("vccesantias"));
//        cert.setMensaje((String) query.getOutputParameterValue("vcmensaje"));
//        cert.setOtroMensaje((String) query.getOutputParameterValue("vcotromensaje"));
//        cert.setMonto((String) query.getOutputParameterValue("vcmonto"));
//        cert.setPago((List<Object>) query.getOutputParameterValue("vtpago"));
//        cert.setPrestamos((List<Object>) query.getOutputParameterValue("vprestamos"));
//        cert.setError((String) query.getOutputParameterValue("vcerror"));
//
//        return cert;
//    }




    public CertificatePay getDataCertificatedPay(Long codCertificadoEmpresa, Long numeroContrato,
                                                  String periodo, Long radicacion) {
        return new ObtenerComprobantePagoSP(jdbcTemplate)
                .execute(codCertificadoEmpresa, numeroContrato, periodo, radicacion);
    }

    private class ObtenerComprobantePagoSP extends StoredProcedure {

        public ObtenerComprobantePagoSP(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "rhu.qb_portal_rrhh.pb_comprobante_pago");

            declareParameter(new SqlParameter("p_cod_certificado_empresa", Types.NUMERIC));
            declareParameter(new SqlParameter("p_numero_contrato", Types.NUMERIC));
            declareParameter(new SqlParameter("p_periodo", Types.VARCHAR));
            declareParameter(new SqlParameter("p_radicacion", Types.NUMERIC));

            // Parámetros de salida
            declareParameter(new SqlOutParameter("p_nombre_empresa", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_nombre_empresa_fil", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_periodo_nomina", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_consecutivo_hpr", Types.NUMERIC));
            declareParameter(new SqlOutParameter("p_nombre_empleado", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_identificacion_empleado", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_sueldo", Types.NUMERIC));
            declareParameter(new SqlOutParameter("p_ipp", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_rete_fuente", Types.NUMERIC));
            declareParameter(new SqlOutParameter("p_centro_costo", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_ciudad", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_mensaje_basico", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_cesantias", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_mensaje", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_otro_mensaje", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_monto", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_mensaje2", Types.VARCHAR));
            declareParameter(new SqlOutParameter("p_tabla_pago", Types.ARRAY, "RHU.CB_PAGO_TABLA"));
            declareParameter(new SqlOutParameter("p_cursor_prestamos", Types.REF_CURSOR));
            declareParameter(new SqlOutParameter("p_error", Types.VARCHAR));

            compile();
        }

        public CertificatePay execute(Long codCertificadoEmpresa, Long numeroContrato,
                                       String periodo, Long radicacion) {
            Map<String, Object> params = new HashMap<>();
            params.put("p_cod_certificado_empresa", codCertificadoEmpresa);
            params.put("p_numero_contrato", numeroContrato);
            params.put("p_periodo", periodo);
            params.put("p_radicacion", radicacion);

            Map<String, Object> results = super.execute(params);

            CertificatePay comprobante = new CertificatePay();
            String error = (String) results.get("p_error");

            if (error == null || error.isEmpty()) {
                comprobante.setNombreEmpresa((String) results.get("p_nombre_empresa"));
                comprobante.setNombreEmpresaFilial((String) results.get("p_nombre_empresa_fil"));
                comprobante.setPeriodoNomina((String) results.get("p_periodo_nomina"));
                comprobante.setConsecutivoHpr(((Number) results.get("p_consecutivo_hpr")).longValue());
                comprobante.setNombreEmpleado((String) results.get("p_nombre_empleado"));
                comprobante.setIdentificacionEmpleado((String) results.get("p_identificacion_empleado"));
                comprobante.setSueldo(((Number) results.get("p_sueldo")).longValue());
                comprobante.setIpp((String) results.get("p_ipp"));
                comprobante.setRtefte(((Number) results.get("p_rete_fuente")).longValue());
                comprobante.setCentroCosto((String) results.get("p_centro_costo"));
                comprobante.setNombreCiudad((String) results.get("p_ciudad"));
                comprobante.setMensajeBasico((String) results.get("p_mensaje_basico"));
                comprobante.setCesantias((String) results.get("p_cesantias"));
                comprobante.setMensaje((String) results.get("p_mensaje"));
                comprobante.setOtroMensaje((String) results.get("p_otro_mensaje"));
                comprobante.setMonto((String) results.get("p_monto"));
                comprobante.setMensaje2((String) results.get("p_mensaje2"));

                Array arrPago = (Array) results.get("p_tabla_pago");
                if (arrPago != null) {
                    try {
                        // Asegúrate de que sea de Oracle
                        oracle.sql.ARRAY oracleArray = (oracle.sql.ARRAY) arrPago;
                        Object[] data = (Object[]) oracleArray.getArray();

                        List<RegisterPay> pagos = new ArrayList<>();
                        for (Object item : data) {
                            oracle.sql.STRUCT struct = (oracle.sql.STRUCT) item;
                            Object[] attributes = struct.getAttributes();

                            RegisterPay rp = new RegisterPay();
                            rp.setConcepto((String) attributes[0]);
                            rp.setCantidad((BigDecimal) attributes[1]);
                            rp.setValor((BigDecimal) attributes[2]);
                            rp.setTipo((String) attributes[3]);
                            pagos.add(rp);
                        }
                        comprobante.setTablaPago(pagos);
                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                        throw new RuntimeException("Error al procesar RHU.CB_PAGO_TABLA", e);
                    }
                }


                // Procesar array de pagos
//                Array arrPago = (Array) results.get("p_tabla_pago");
//                System.out.println(arrPago.toString());
//                List<RegisterPay> pagos = new ArrayList<>();
//
//                if (arrPago != null) {
//                    try {
//                        System.out.println("aca deberia");
//                        Object[] tablaPago = (Object[]) arrPago.getArray();
//                        System.out.println("aca");
//                        System.out.println(tablaPago);
//                        for (Object item : tablaPago) {
//                            Struct structItem = (Struct) item;
//                            Object[] reg = structItem.getAttributes();
//
//                            RegisterPay rp = new RegisterPay();
//                            rp.setConcepto((String) reg[0]);
//                            rp.setCantidad((BigDecimal) reg[1]);
//                            rp.setValor((BigDecimal) reg[2]);
//                            rp.setTipo((String) reg[3]);
//                            pagos.add(rp);
//                        }
//                    } catch (SQLException e) {
//                        System.out.println(e.getMessage());
//                        throw new RuntimeException("Error procesando array de pagos", e);
//                    }
//                }
//                comprobante.setTablaPago(pagos);

                // Procesar cursor de préstamos
                try (ResultSet rs = (ResultSet) results.get("p_cursor_prestamos")) {
                    while (rs != null && rs.next()) {
                        LoanPay pa = new LoanPay();
                        pa.setPreCodigo(rs.getLong("pre_codigo"));
                        pa.setPreSaldo(rs.getDouble(4));
                        pa.setPreValor(rs.getDouble("pre_valor"));
                        pa.setTrpNombre(rs.getString("tpr_nombre"));
                        comprobante.getTablaAhorroPrestamo().add(pa);
                    }
                } catch (SQLException e) {
                    System.out.println("llega aca");
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("acaa ");
                System.out.println(error);
                comprobante.setError(error);
            }

            return comprobante;
        }
    }

}
