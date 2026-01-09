package com.microcode.client.service.helper;

import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.sql.Clob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HelperService {

    public String generateCode(){
        String CARACTERES = "0123456789";
        SecureRandom random = new SecureRandom();

        return random.ints(6, 0, CARACTERES.length())
                .mapToObj(i -> String.valueOf(CARACTERES.charAt(i)))
                .reduce((a, b) -> a + b)
                .orElse("");
    }

    public boolean isPrincipal(Long empNdFil){
        if(empNdFil == null)return false;
        List<Long> emp = List.of(830057687L, 860090915L, 800148972L, 800165661L, 800141699L);
        return emp.contains(empNdFil);
    }

    public String generateMail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex == email.length() - 1) return email;

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        String maskedUsername = mask(username);

        int visibleDomainChars = 7;
        String maskedDomain;
        if (domain.length() <= visibleDomainChars) {
            maskedDomain = "*".repeat(domain.length());
        } else {
            int maskLength = domain.length() - visibleDomainChars;
            maskedDomain = "*".repeat(maskLength) + domain.substring(maskLength);
        }

        return maskedUsername + "@" + maskedDomain;
    }

    private String mask(String text) {
        int visible = text.length() <= 4 ? 1 : 3;
        int masked = text.length() - visible;
        if (masked <= 0) return text;
        return text.substring(0, visible) + "*".repeat(masked);
    }






    public Boolean getDateCertificateAvailable() {
        LocalDate now = LocalDate.now();
        return now.getMonthValue() >= 4;
    }

    public Integer getYearCertificate() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int lastYear = currentYear - 1;;
        return lastYear;
    }

    public String getDateCertifiedDianStartDate() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int lastYear = currentYear - 1;;
        return LocalDate.of(lastYear, 1, 1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String getDateCertifiedDianEndDate() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int lastYear = currentYear - 1;;
        return LocalDate.of(lastYear, 12, 31).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public List<Long> definePrincipalForCode(Integer companyId){
        if(companyId == 2) return List.of(800148972L);
        if(companyId == 3) return List.of(830057687L);
        return List.of(860090915L);
    }

    public Long definePrincipalForCodeUnique(Integer companyId){
        if(companyId == 2) return 800148972L;
        if(companyId == 3) return 830057687L;
        return 860090915L;
    }

    public Long defineUniquePrincipalForAuthorized(List<Long> authorized){
        if(authorized.contains(800148972L)) return 2L;
        if(authorized.contains(830057687L)) return 3L;
        return 1L;
    }

    public String defineUniquePrincipalForAuthorizedString(List<Long> authorized){
        if(authorized.contains(800148972L)) return "Serviola SAS";
        if(authorized.contains(830057687L)) return "Atecno SAS";
        return "Activos SAS ";
    }

    public String clobToString(Clob clob) {
        if (clob == null) return null;
        try (Reader reader = clob.getCharacterStream();
             StringWriter writer = new StringWriter()) {
            char[] buffer = new char[2048];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }
            return writer.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String getDataExtractLine(String text) {
        try{
            if (text == null || text.isEmpty()) return "";
            int index = text.indexOf('-');
            if (index > 0)
                return text.substring(0, index).toLowerCase();
            else return "";
        }catch (Exception e){
            return "";
        }
    }

    public String defineChatType(Integer typeChat){
        if(typeChat == 1){
            return """
                    A través de este chat podrás solicitar:
                    ◾Documentos 📄
                    ◾Estado de Liquidación 💰
                    ◾Requisitos y Trámites Entidades 📰
                    ◾Bienestar 👬
                    
                    Para continuar ✨ por favor envíame *Tipo* y *Numero de documento*.
                    """;
        }else{
            return """
                    A través de este chat podrás solicitar:
                    ◾Cotiza Servicios ✍️
                    ◾Confirmar Referencias Laborales ✅
                    ◾Ofertas Laborales 📰
                    ◾Consultar Portal HV 💻
                    
                    Para continuar ✨ por favor envíame *Tipo de documento* , *Numero de documento*, *Nombres*, *Celular* y *Correo electrónico*.
                    """;
        }
    }

}
