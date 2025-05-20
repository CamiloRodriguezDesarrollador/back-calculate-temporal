package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.AllArgsConstructor;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.STRUCT;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.sql.ARRAY;
import oracle.sql.Datum;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Service
@AllArgsConstructor
public class CertificatesService {

    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public CertificateJob getDataCertificatedJob(Long nmcteCodigo, Long nmctoNumero, String vcpromediasueldo, String vcdestinatario) {
        try{

            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("rhu.qb_portal_rrhh.pb_certificacion_laboral");

            query.registerStoredProcedureParameter("nmcte_codigo", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmcto_numero", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcpromediasueldo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcdestinatario", String.class, ParameterMode.IN);

            query.setParameter("nmcte_codigo", nmcteCodigo);
            query.setParameter("nmcto_numero", nmctoNumero);
            query.setParameter("vcpromediasueldo", vcpromediasueldo);
            query.setParameter("vcdestinatario", vcdestinatario);

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
        }catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Long> getDataNumbersRads(Long nmctoNumero,Long nmcteCodigo, String vcfechapago ) {
        try{

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("QB_PORTAL_RRHH")
                    .withSchemaName("RHU")
                    .withProcedureName("PL_CARGAR_RADICACION_PERIODO")
                    .withoutProcedureColumnMetaDataAccess()

                    .declareParameters(
                            new SqlParameter("nmctoNumero", Types.NUMERIC),
                            new SqlParameter("nmtne_codigo", Types.NUMERIC),
                            new SqlParameter("vcfechapago", Types.VARCHAR),
                            new SqlOutParameter("vcconsulta", OracleTypes.CURSOR, (RowMapper<Long>) (rs, rowNum) -> rs.getLong(1))
                    );

            Map<String, Object> result = jdbcCall.execute(
                    new MapSqlParameterSource()
                            .addValue("nmctoNumero", nmctoNumero)
                            .addValue("nmtne_codigo", nmcteCodigo)
                            .addValue("vcfechapago", vcfechapago)
            );

            @SuppressWarnings("unchecked")
            List<Long> rads = (List<Long>) result.get("vcconsulta");
            return rads;
        }catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public CertificatePay getDataCertificatedPay(Long codCertificadoEmpresa, Long numeroContrato,
                                                 String periodo, Long radicacion) throws SQLException {
        try{
            StoredProcedure sp = new StoredProcedure(jdbcTemplate, "rhu.qb_portal_rrhh.pb_comprobante_pago") {
                {
                    declareParameter(new SqlParameter("nmcte_codigo", Types.NUMERIC));
                    declareParameter(new SqlParameter("nmcto_numero", Types.NUMERIC));
                    declareParameter(new SqlParameter("vcrangofechas", Types.VARCHAR));
                    declareParameter(new SqlParameter("nmradicacion", Types.NUMERIC));

                    declareParameter(new SqlOutParameter("vcnomempresa", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcnomempresafil", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcperiodonomina", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("nmconsecutivohpr", Types.NUMERIC));
                    declareParameter(new SqlOutParameter("vcnomempleado", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcepl_identificacion", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("nmsueldo", Types.NUMERIC));
                    declareParameter(new SqlOutParameter("vcipp", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("nmrtefte", Types.NUMERIC));
                    declareParameter(new SqlOutParameter("vccentrocosto", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcnombreciudad", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcmensajebasico", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vccesantias", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcmensaje", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcotromensaje", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcmonto", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vcMensaje2", Types.VARCHAR));
                    declareParameter(new SqlOutParameter("vtpago", Types.ARRAY, "RHU.CB_PAGO_TABLA"));
                    declareParameter(new SqlOutParameter("vprestamos", Types.REF_CURSOR));
                    declareParameter(new SqlOutParameter("p_error", Types.VARCHAR));
                    compile();
                }
            };

            Map<String, Object> params = new HashMap<>();
            params.put("nmcte_codigo", codCertificadoEmpresa);
            params.put("nmcto_numero", numeroContrato);
            params.put("vcrangofechas", periodo);
            params.put("nmradicacion", radicacion);

            Map<String, Object> results = sp.execute(params);

            CertificatePay cert = new CertificatePay();
            String error = (String) results.get("p_error");

            if (error == null || error.isEmpty()) {
                cert.setNombreEmpresa((String) results.get("vcnomempresa"));
                cert.setNombreEmpresaFilial((String) results.get("vcnomempresafil"));
                cert.setPeriodoNomina((String) results.get("vcperiodonomina"));
                cert.setConsecutivoHpr(((Number) results.get("nmconsecutivohpr")).longValue());
                cert.setNombreEmpleado((String) results.get("vcnomempleado"));
                cert.setIdentificacionEmpleado((String) results.get("vcepl_identificacion"));
                cert.setSueldo(((Number) results.get("nmsueldo")).longValue());
                cert.setIpp((String) results.get("vcipp"));
                cert.setRtefte(((Number) results.get("nmrtefte")).longValue());
                cert.setCentroCosto((String) results.get("vccentrocosto"));
                cert.setNombreCiudad((String) results.get("vcnombreciudad"));
                cert.setMensajeBasico((String) results.get("vcmensajebasico"));
                cert.setCesantias((String) results.get("vccesantias"));
                cert.setMensaje((String) results.get("vcmensaje"));
                cert.setOtroMensaje((String) results.get("vcotromensaje"));
                cert.setMonto((String) results.get("vcmonto"));
                cert.setMensaje2((String) results.get("vcMensaje2"));

                Array arrPago = (Array) results.get("vtpago");
                List<RegisterPay> pagos = getRegisterPays(arrPago);
                cert.setTablaPago(pagos);

                try {
                    List<Map<String, Object>> prestamos = (List<Map<String, Object>>) results.get("vprestamos");
                    for (Map<String, Object> row : prestamos) {
                        LoanPay pa = new LoanPay();
                        pa.setPreCodigo(((Number) row.get("PRE_CODIGO")).longValue());
                        pa.setPreSaldo(((Number) row.get("PRE_SALDO")).doubleValue());
                        pa.setPreValor(((Number) row.get("PRE_VALOR")).doubleValue());
                        pa.setTrpNombre((String) row.get("TPR_NOMBRE"));
                        cert.getTablaAhorroPrestamo().add(pa);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }

            return cert;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }


    }

    public String getDataCertificatedDian(
            String tdcTd, Long empNd, String tdcTdEpl, Long eplNd, String fechaIncio, String fechaFin) {
        try{

            String P_FIRMA = "/opt/SGD/reportes/servidor_impresion/img/firma_" + empNd + ".jpg";
            String password = Long.toString(eplNd);
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("rep.QB_GENERAR_REPORTE_V2.pb_generar_reporte");

            query.registerStoredProcedureParameter("vcReporte", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcValores", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcUsuario", String.class, ParameterMode.IN);

            query.setParameter("vcReporte", "RRHU6312");
            query.setParameter("vcValores", tdcTd + "," + empNd + "," + tdcTdEpl + "," + eplNd + "," + fechaIncio + "," + fechaFin + ",TODOS,S,2," + P_FIRMA + "," + password);
            query.setParameter("vcUsuario", "INTRAUSER");

            query.registerStoredProcedureParameter("vcSalida", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("vcerror", String.class, ParameterMode.OUT);

            query.execute();

            String response;
            response = ((String) query.getOutputParameterValue("vcSalida"));
            String error = ((String) query.getOutputParameterValue("vcerror"));
            System.out.println("error cert ingresos:" + error);

            if(error != null) return null;
            return response;
        }catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }


    private static List<RegisterPay> getRegisterPays(Array arrPago) throws SQLException {
        List<RegisterPay> pagos = new ArrayList<>();
        if (arrPago != null) {
            Object[] tablaPago = (Object[]) arrPago.getArray();
            for (Object obj : tablaPago) {
                Object[] reg = ((STRUCT) obj).getAttributes();
                RegisterPay rp = new RegisterPay();
                rp.setConcepto((String) reg[0]);
                rp.setCantidad((BigDecimal) reg[1]);
                rp.setValor((BigDecimal) reg[2]);
                rp.setTipo((String) reg[3]);
                pagos.add(rp);
            }
        }
        return pagos;
    }

    public String getStatusLiq(String tdcTdEpl,Long eplNd ) {
        try{

            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("rep.QB_PROCESO_LIQ.PL_ESTADO_LIQ");

            query.registerStoredProcedureParameter("vcTd_td_epl", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmEpl_nd", Long.class, ParameterMode.IN);

            query.setParameter("vcTd_td_epl", tdcTdEpl);
            query.setParameter("nmEpl_nd", eplNd);

            query.registerStoredProcedureParameter("vcEstado", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("vcError", String.class, ParameterMode.OUT);

            query.execute();

            String error = ((String) query.getOutputParameterValue("vcError"));
            if(error != null) return null;

            return (String) query.getOutputParameterValue("vcEstado");
        }catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getDataCertificateDian() {
        try{

            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("RHU.QB_AUTOLIQUIDACION_JRHU0034.pl_wsdl_autoliquidacion_epl");

            query.registerStoredProcedureParameter("vctdc_td", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmEmp_nd", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vctdc_td_fil", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmEmp_nd_fil", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vctdc_td_epl", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmepl_nd", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmtmocode", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmTrecode", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vctpq_periodo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcaud_usuario", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcfecha_trans", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcbandera", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcciu_nombre", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcsuc_nombre_fil", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vccct_nombre", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcconsumo_masivo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmAcm_Codigo", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmNum_Planilla", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmtio_codigo", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcTipoFiltro", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcValorFiltro", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcTiempoEstimado", String.class, ParameterMode.IN);

            query.setParameter("vctdc_td","NI");
            query.setParameter("nmEmp_nd",860090915L);
            query.setParameter("vctdc_td_fil","NI");
            query.setParameter("nmEmp_nd_fil",860090915L);
            query.setParameter("vctdc_td_epl","CC");
            query.setParameter("nmepl_nd",1015459785L);
            query.setParameter("nmtmocode",1L);
            query.setParameter("nmTrecode",1L);
            query.setParameter("vctpq_periodo","202504");
            query.setParameter("vcaud_usuario","1015459785");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String dateTrans = simpleDateFormat.format(new Date());
            query.setParameter("vcfecha_trans",dateTrans);
            query.setParameter("vcbandera","0");
            query.setParameter("vcciu_nombre","");
            query.setParameter("vcsuc_nombre_fil","");
            query.setParameter("vccct_nombre","");
            query.setParameter("vcconsumo_masivo","N");
            query.setParameter("nmAcm_Codigo",0L);
            query.setParameter("nmNum_Planilla",0L);
            query.setParameter("nmtio_codigo",2L);
            query.setParameter("vcTipoFiltro", "");
            query.setParameter("vcValorFiltro", "");
            query.setParameter("vcTiempoEstimado", "");


            query.registerStoredProcedureParameter("vcENDPOINT", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("vcXMLINPUT", Clob.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("vcerror", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("vcmensaje", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("nmTpq_Code", Long.class, ParameterMode.OUT);


            query.execute();

            String endpoint = (String) query.getOutputParameterValue("vcENDPOINT");
            Clob xmlInputClob = (Clob) query.getOutputParameterValue("vcXMLINPUT");
            String error = (String) query.getOutputParameterValue("vcerror");
            String mensaje = (String) query.getOutputParameterValue("vcmensaje");
            Long tpqCode = (Long) query.getOutputParameterValue("nmTpq_Code");

            System.out.println(endpoint);
            System.out.println(xmlInputClob);
            System.out.println(error);
            System.out.println(mensaje);
            System.out.println(tpqCode);
            if (error == null || error.isEmpty()) {
                System.out.println("EWntra aca");
            }
            System.out.println(query);
            return "";
        }catch (Exception e ){
            System.out.println("Es este error");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getDataCertificateDianInsert() {
        try{

            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("RHU.QB_AUTOLIQUIDACION_JRHU0034.pl_ins_transaccion_int_autol");

            query.registerStoredProcedureParameter("vctia_descripcion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vctdc_td_epl", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmepl_nd", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vctdc_td", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmemp_nd", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vctdc_td_fil", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmemp_nd_fill", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmgpr_code", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcperiodo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("nmtre_code", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("AUD_USUARIO", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("vcbandera", String.class, ParameterMode.IN);

            query.setParameter("vctia_descripcion","Reporte inicial");
            query.setParameter("vctdc_td_epl","CC");
            query.setParameter("nmepl_nd",1015459785L);
            query.setParameter("vctdc_td","NI");
            query.setParameter("nmemp_nd",860090915L);
            query.setParameter("vctdc_td_fil","NI");
            query.setParameter("nmemp_nd_fill",860090915L);
            query.setParameter("nmgpr_code",0L);
            query.setParameter("vcperiodo","202504");
            query.setParameter("nmtre_code",1L);
            query.setParameter("AUD_USUARIO","1015459785");
            query.setParameter("vcbandera","S");


            query.registerStoredProcedureParameter("vcestado_proceso", String.class, ParameterMode.INOUT);
            query.registerStoredProcedureParameter("vcmensaje_proceso", String.class, ParameterMode.INOUT);

            query.setParameter("vcestado_proceso", "");
            query.setParameter("vcmensaje_proceso", "");


            query.execute();

            String vcestadoProceso = (String) query.getOutputParameterValue("vcestado_proceso");
            String vcmensajeProceso = (String) query.getOutputParameterValue("vcmensaje_proceso");

            System.out.println(vcestadoProceso);
            System.out.println(vcmensajeProceso);
            return "";
        }catch (Exception e ){
            System.out.println("Es este error");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getDataCertificateDianKey() {
        try{

            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("RHU.QB_AUTOLIQUIDACION_JRHU0034.pb_fil_periodo_epl");

            query.registerStoredProcedureParameter("VCTDC_TD", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("NMEPL_ND", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("PERIODO_FIL", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("VCTRECODE", Long.class, ParameterMode.IN);

            query.setParameter("VCTDC_TD","CC");
            query.setParameter("NMEPL_ND",1015459785L);
            query.setParameter("PERIODO_FIL","202503");
            query.setParameter("VCTRECODE",0L);


            query.registerStoredProcedureParameter("VCPERIODO", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("VCREQ", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("VCKEYPPAL", String.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("VCGPE_CODE", Long.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("vcestado_proceso", String.class, ParameterMode.INOUT);
            query.registerStoredProcedureParameter("vcmensaje_proceso", String.class, ParameterMode.INOUT);
            query.registerStoredProcedureParameter("VCTRE_CODE", Long.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("NMTPQ_CODE", Long.class, ParameterMode.OUT);

            query.setParameter("vcestado_proceso", "");
            query.setParameter("vcmensaje_proceso", "");


            query.execute();

            String vcPeriod = (String) query.getOutputParameterValue("VCPERIODO");
            String vcReq = (String) query.getOutputParameterValue("VCREQ");
            String vcKey = (String) query.getOutputParameterValue("VCKEYPPAL");
            Long vcGpe = (Long) query.getOutputParameterValue("VCGPE_CODE");
            Long vcTre = (Long) query.getOutputParameterValue("VCTRE_CODE");
            Long nmtpqCode = (Long) query.getOutputParameterValue("NMTPQ_CODE");
            String vcestadoProceso = (String) query.getOutputParameterValue("vcestado_proceso");
            String vcmensajeProceso = (String) query.getOutputParameterValue("vcmensaje_proceso");

            System.out.println(vcPeriod);
            System.out.println(vcReq);
            System.out.println(vcKey);
            System.out.println(vcGpe);
            System.out.println(vcTre);
            System.out.println(nmtpqCode);
            System.out.println(vcestadoProceso);
            System.out.println(vcmensajeProceso);
            return "";
        }catch (Exception e ){
            System.out.println("Es este error");
            System.out.println(e.getMessage());
            return null;
        }
    }

}
