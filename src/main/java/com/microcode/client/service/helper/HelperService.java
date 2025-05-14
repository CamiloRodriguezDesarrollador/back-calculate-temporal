package com.microcode.client.service.helper;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HelperService {

    public String generateCode(){
//        String CARACTERES = "abcdefghijklmnopqrstuvwxyz0123456789";
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
        if (atIndex <= 0) return email;

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        String visiblePart;
        String maskedPart;

        if (username.length() <= 4) {
            visiblePart = username.substring(0, 1);
            maskedPart = "*".repeat(username.length() - 1);
        } else {
            visiblePart = username.substring(0, 3);
            maskedPart = "*".repeat(username.length() - 3);
        }

        return visiblePart + maskedPart + domain;
    }

    public List<String> getPeriodLabels(String perSigla) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> labels = new ArrayList<>();

        if ("M".equals(perSigla)) {
            for (int i = 3; i >= 1; i--) {
                LocalDate month = today.minusMonths(i);
                labels.add(fmt.format(month.withDayOfMonth(1)) + " - " +
                        fmt.format(month.withDayOfMonth(month.lengthOfMonth())));
            }
        } else if ("Q".equals(perSigla)) {
            List<String> tempLabels = new ArrayList<>();
            LocalDate ref;

            if (today.getDayOfMonth() < 16) {
                ref = today.minusMonths(1).withDayOfMonth(16);
            } else {
                ref = today.withDayOfMonth(1);
            }

            while (tempLabels.size() < 6) {
                LocalDate start;
                LocalDate end;

                if (ref.getDayOfMonth() == 1) {
                    start = ref.withDayOfMonth(1);
                    end = ref.withDayOfMonth(15);
                    ref = ref.minusMonths(1).withDayOfMonth(16);
                } else {
                    // Segunda quincena
                    start = ref.withDayOfMonth(16);
                    end = ref.withDayOfMonth(ref.lengthOfMonth());
                    ref = ref.withDayOfMonth(1);
                }

                tempLabels.add(fmt.format(start) + " - " + fmt.format(end));
            }

            Collections.reverse(tempLabels);
            labels.addAll(tempLabels);
        }

        return labels;
    }

    public String getDateCertifiedDianStartDate() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int lastYear;

        if (now.getMonthValue() > 3) { // después de marzo
            lastYear = currentYear - 1;
        } else {
            lastYear = currentYear - 2;
        }

        return LocalDate.of(lastYear, 1, 1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String getDateCertifiedDianEndDate() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int lastYear;

        if (now.getMonthValue() > 3) { // después de marzo
            lastYear = currentYear - 1;
        } else {
            lastYear = currentYear - 2;
        }
        return LocalDate.of(lastYear, 12, 31).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String getEmailByNit(String nitInput) {

        Map<String, String> nitToEmail = new HashMap<>();
        nitToEmail.put("800003122", "auxmonteria@activos.com.co, monteria@activos.com.co");
        nitToEmail.put("890900841", "afiliacionesmedellin@activos.com.co, medellin@activos.com.co");
        nitToEmail.put("890900842", "afiliacionesmedellin@activos.com.co, medellin@activos.com.co");
        nitToEmail.put("800219488", "contratoscucuta@activos.com.co, cucuta@activos.com.co");
        nitToEmail.put("890101994", "mcpallares@activos.com.co, barranquilla@activos.com.co");
        nitToEmail.put("890480023", "alistamientocartagena@activos.com.co, cartagena@activos.com.co");
        nitToEmail.put("891800213", "auxrrhhtunja@activos.com.co, tunja@activos.com.co");
        nitToEmail.put("890806490", "contratospereira@activos.com.co, pereira@activos.com.co, dacarrillo@activos.com.co, ibague@activos.com.co");
        nitToEmail.put("891190047", "contratosneiva@activos.com.co, neiva@activos.com.co");
        nitToEmail.put("844003392", "auxmonteria@activos.com.co, monteria@activos.com.co");
        nitToEmail.put("891500182", "auxmonteria@activos.com.co, monteria@activos.com.co");
        nitToEmail.put("892399989", "cartagena@activos.com.co, alistamientocartagena@activos.com.co");
        nitToEmail.put("891600091", "afiliacionesmedellin@activos.com.co, medellin@activos.com.co");
        nitToEmail.put("891080005", "auxmonteria@activos.com.co, monteria@activos.com.co");
        nitToEmail.put("860013570", "auxccf@activos.com.co, auxccf2@activos.com.co");
        nitToEmail.put("860007336", "auxccf@activos.com.co, auxccf2@activos.com.co");
        nitToEmail.put("860066942", "auxccf@activos.com.co, auxccf2@activos.com.co");
        nitToEmail.put("800231969", "auxccf@activos.com.co, chernandez@activos.com.co");
        nitToEmail.put("892115006", "cartagena@activos.com.co, alistamientocartagena@activos.com.co");
        nitToEmail.put("891180008", "contratosneiva@activos.com.co, neiva@activos.com.co");
        nitToEmail.put("891780093", "cartagena@activos.com.co, alistamientocartagena@activos.com.co");
        nitToEmail.put("892000146", "villavicencio@activos.com.co, decastro@activos.com.co");
        nitToEmail.put("891280008", "contratospasto@activos.com.co, sucursalpasto@activos.com.co");
        nitToEmail.put("890500516", "contratoscucuta@activos.com.co, cucuta@activos.com.co");
        nitToEmail.put("891200337", "contratospasto@activos.com.co, pasto@activos.com.co");
        nitToEmail.put("890000381", "contratospereira@activos.com.co, pereira@activos.com.co");
        nitToEmail.put("891480000", "contratospereira@activos.com.co, pereira@activos.com.co");
        nitToEmail.put("892400320", "alistamientocartagena@activos.com.co, cartagena@activos.com.co");
        nitToEmail.put("890200106", "auxrrhhbmanga@activos.com.co, bucaramanga@activos.com.co");
        nitToEmail.put("890201578", "auxrrhhbmanga@activos.com.co, bucaramanga@activos.com.co");
        nitToEmail.put("892200015", "cartagena@activos.com.co, alistamientocartagena@activos.com.co");
        nitToEmail.put("890700148", "rrhhibague@activos.com.co, ibague@activos.com.co");
        nitToEmail.put("890303093", "liznieto@activos.com.co, cali@activos.com.co");
        nitToEmail.put("890303208", "liznieto@activos.com.co, cali@activos.com.co");

        return nitToEmail.getOrDefault(nitInput, "por favor revisa con tu empresa el correo al cual enviar.");
    }

    public String getEmailEpsPrincipal(Long empNd, String type){
        System.out.println(empNd);
        if(type.equals("AFP")){
            if(empNd == 830057687L)return "asesorafp@atecno.com.co";
            if(empNd == 800148972L)return "asesorafp@serviola.com.co";
            return "asesorafp@activos.com.co";
        }
        if(type.equals("EPS")){
            if(empNd == 830057687L)return "atencioneps@atecno.com.co";
            if(empNd == 800148972L)return "atencioneps@serviola.com.co";
            return "atencioneps@activos.com.co";
        }
        else return "";
    }

    public String getUrlForPrincipal(Long empNd){
        if(empNd == 830057687L)return "https://apps.genialw.com/SitioTrabajador/index5.jsp";
        if(empNd == 800148972L)return "https://apps.genialw.com/SitioTrabajador/index3.jsp";
        else return "https://apps.genialw.com/SitioTrabajador/index1.jsp";
    }

}
