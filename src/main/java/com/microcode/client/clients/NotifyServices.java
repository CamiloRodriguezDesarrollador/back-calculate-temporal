package com.microcode.client.clients;

import com.microcode.client.controller.secutiry.Env;
import com.microcode.client.service.helper.HelperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotifyServices {

    @Value("${notify.api.url}")
    public String urlNotify;

    @Value("${api.key.notify}")
    public String apyKeyNotify;

    public HelperService helperService;

    public NotifyServices() {
        this.helperService = new HelperService();
    }

    public void sendMailChat(String emailTo, String contentMail, String subject, List<Long>  authorized) {
        WebClient webClient = Env.withCurrentHeaders(WebClient.builder()).build();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Long principalAuthorized = helperService.defineUniquePrincipalForAuthorized(authorized);

        body.add("channel", "MAIL");
        body.add("principal", principalAuthorized);
        body.add("to", emailTo);
        body.add("subject", subject);
        body.add("message", templateHtml(contentMail,subject,principalAuthorized));
        body.add("process","15" );

        webClient.post()
                .uri(urlNotify + "/send")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-Current-Token",apyKeyNotify)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->Mono.error(new RuntimeException(errorBody)))
                )
                .bodyToMono(String.class)
                .doOnError(ex ->
                        log.error("ERROR: {}" , ex.getMessage())
                ).subscribe();
    }

    public void sendMailCertificatesFile(String contentMail, String subject , String emailTo, byte[] fileBytes, String filename,
                                                 List<Long> authorized) throws IOException {
        WebClient webClient = Env.withCurrentHeaders(WebClient.builder()).build();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Long principalAuthorized = helperService.defineUniquePrincipalForAuthorized(authorized);


        body.add("channel", "MAIL");
        body.add("principal", principalAuthorized);
        body.add("to", emailTo);
        body.add("subject", subject);
        body.add("message", templateHtml(contentMail,subject,principalAuthorized));
        body.add("nameFile", filename);
        body.add("typeFile", "pdf");
        body.add("application","App Teo" );
        body.add("process","15" );


        body.add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        webClient.post()
                .uri(urlNotify + "/send")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-Current-Token",apyKeyNotify)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->Mono.error(new RuntimeException(errorBody)))
                )
                .bodyToMono(String.class)
                .doOnError(ex ->
                        log.error("ERROR: {}" , ex.getMessage())

                ).subscribe();
    }

    public void sendMailChatJust(String emailTo, String contentMail, String subject, List<Long>  authorized) {
        WebClient webClient = Env.withCurrentHeaders(WebClient.builder()).build();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Long principalAuthorized = helperService.defineUniquePrincipalForAuthorized(authorized);

        body.add("channel", "MAIL");
        body.add("principal", principalAuthorized);
        body.add("to", emailTo);
        body.add("toCc", "sac@fedac.co");
        body.add("subject", subject);
        body.add("message", templateHtmlJust(contentMail));
        body.add("process","15" );

        webClient.post()
                .uri(urlNotify + "/send")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-Current-Token",apyKeyNotify)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->Mono.error(new RuntimeException(errorBody)))
                )
                .bodyToMono(String.class)
                .doOnError(ex ->
                        log.error("ERROR: {}" , ex.getMessage())
                ).subscribe();
    }

    public String templateHtml(String contentMail, String subject, Long idMail ){
        String textTemplate =  "<html>" + """
                        <head>
                        <style>
                        body {
                               font-family: Arial, sans-serif;
                               margin: 0;
                               padding: 0;
                               background-color: #f4f4f4;
                               font-size: 12px;
                             }
                             .container {
                               max-width: 650px;
                               margin: 30px auto;
                               background: #fff;
                               border-radius: 15px;
                               box-shadow: 0px 4px 12px rgba(0,0,0,0.1);
                               display: flex;
                               border: 1px solid rgb(121, 121, 121);
                             }
                             .left{
                                 display: grid;
                                 place-content: center;
                                 background: linear-gradient(to bottom, #0a1d3e 0, #19386e, #0a1d3e);
                                 width: 400px;
                             }
                             .left img {
                               width: 160px;
                             }
                             .code-box {
                                 font-size: 15px;
                                 font-weight: bold;
                                 letter-spacing: 10px;
                                 color: #2c3e50;
                                 padding: 10px;
                                 text-align: center;
                                 border-radius: 8px;
                                 border: 1px solid #ccc;
                             }
                             .right{
                                 padding: 20px;
                             }
                             .footer{
                                 color: gray;
                                 font-size: 10px;
                                 font-style: italic;
                             }
                        </style>
                        </head>
                        <body>
                           <div class="container">
                              <div class="left">
                                  <img src="%s" alt="">
                              </div>
                              <div class="right">
                                  <div class="content-imagen-ppal" style="display: flex">
                                      <h4 style="width:400px; margin-bottom: -20px;">%s</h4>
                                      <img src="%s" style="height:30px">
                                  </div>
                                  %s
                                  <p>Gracias por tu atención.</p>
                                  <p class="footer">
                                      Este es un correo generado automáticamente, por favor no respondas a este mensaje. Si no has solicitado este correo electrónico, por favor contacta con el administrador. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte.
                                  </p>
                                </p>
                              </div>
                            </div>
                        </body>
                        </html>""";

       return String.format(textTemplate, defineIcon(idMail,subject),subject,defineImage(idMail), contentMail);

    }

    public String templateHtmlJust(String contentMail){
        String textTemplate  = "<html>" + """
                        <head>
                        <style>
                       body {
                           font-family: Arial, sans-serif;
                           margin: 0;
                           padding: 0;
                           background-color: #f4f4f4;
                           font-size: 12px;
                         }
                         .footer{
                             color: gray;
                             font-size: 10px;
                             font-style: italic;
                         }
                         .link{
                             margin-top: 15px;
                             display: block;
                             color: #1a73e8;
                             text-decoration: none;
                             font-weight: bold;
                         }
                        </style>
                        </head>
                        <body>
                           <div class="container">
                              <div class="right">
                                  %s
                                  <p>Gracias por tu atención.</p>
                                  <p class="footer">
                                      Este es un correo generado automáticamente, por favor no respondas a este mensaje. Si no has solicitado este correo electrónico, por favor contacta con el administrador. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte.
                                  </p>
                                </p>
                              </div>
                            </div>
                        </body>
                        </html>""";


        return String.format(textTemplate,contentMail);

    }


    private static final Map<String, String> ICON_MAP = new LinkedHashMap<>();

    static {
        ICON_MAP.put("verificación", "VERIFICACION");
        ICON_MAP.put("pago", "PAGO");
        ICON_MAP.put("laboral", "LABORAL");
        ICON_MAP.put("ingresos", "INGRESOS");
        ICON_MAP.put("ccf", "CAJAS");
    }

    public String defineImage(Long idMail){
        if(idMail == 3) return "https://storage.googleapis.com/bucket_apps_public/Logos/ATECNO.png";
        if(idMail == 2) return "https://storage.googleapis.com/bucket_apps_public/Logos/SERVIOLA.png";
        return "https://storage.googleapis.com/bucket_apps_public/Logos/ACTIVOS.png";
    }

    public String defineIcon(Long idMail,String subject){
        try{
            String text ;

            String sub = subject.toLowerCase();

            text = ICON_MAP.entrySet()
                    .stream()
                    .filter(entry -> sub.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse("INGRESOS");

            return "https://storage.googleapis.com/bucket_apps_public/IconosCorreo/" + idMail + "-" + text + ".png";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "https://storage.googleapis.com/bucket_apps_public/IconosCorreo/" + idMail + "-INGRESOS.png";
        }
    }


    public void sendNotifyChat(String text) {
        WebClient webClient = Env.withCurrentHeaders(WebClient.builder()).build();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("channel", "CHAT");
        body.add("principal", 860090915);
        body.add("to", "AAQA1c87pDg");
        body.add("message", text);
        body.add("process","15" );

        webClient.post()
                .uri(urlNotify + "/send")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-Current-Token",apyKeyNotify)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->Mono.error(new RuntimeException(errorBody)))
                )
                .bodyToMono(String.class)
                .doOnError(ex ->
                        log.error("ERROR: {}" , ex.getMessage())
                ).subscribe();
    }

}
