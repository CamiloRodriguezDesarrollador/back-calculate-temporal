package com.microcode.client.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class MailServices {

    @Value("${mail.api.url}")
    public String urlMail;

    public WebClient webClient;

    public MailServices() {
        this.webClient = WebClient.builder()
                .build();
    }

    public void sendMailVerified(String emailTo, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("emailTo", emailTo);
        params.add("code", code);
        try{
            this.webClient.post()
                    .uri(urlMail + "/sendMailVerified")
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Mono<String> sendMailCertificates(String employeeName,String typeCertificate, String emailTo, byte[] fileBytes, String filename) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("typeCertificate", typeCertificate);
        body.add("employeeName", employeeName);
        body.add("sendMailCertificates", employeeName);
        body.add("emailTo", emailTo);

        if (fileBytes == null || fileBytes.length == 0) return Mono.empty();

        body.add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        return this.webClient.post()
                .uri(urlMail + "/sendMailCertificates")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(RestClientException.class, ex -> Mono.empty());
    }

    public Mono<String> sendMailInformationCCF(String employeeName, String emailTo,String companyName,
                                         String fileRequirements, String fileDeclaration, String fileVideo) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("employeeName", employeeName);
        params.add("emailTo", emailTo);
        params.add("companyName", companyName);
        params.add("fileRequirements", fileRequirements);
        params.add("fileDeclaration", fileDeclaration);
        params.add("fileVideo", fileVideo);
        try{
            return this.webClient.post()
                    .uri(urlMail +"/sendMailInformationCCF")
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty());
        } catch (Exception e) {
            return null;
        }
    }


//    public Mono<String> sendMailCertificateJob(String employeeName, String emailTo, MultipartFile file) throws IOException {
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("employeeName", employeeName);
//        body.add("emailTo", emailTo);
//        if (file == null) return Mono.empty();
//        body.add("file", new ByteArrayResource(file.getBytes()) {
//            @Override
//            public String getFilename() {
//                return file.getOriginalFilename();
//            }
//        });
//
//        return this.webClient.post()
//                .uri(urlMail + "/sendMailCertificateJob")
//                .bodyValue(body)
//                .retrieve()
//                .bodyToMono(String.class)
//                .onErrorResume(RestClientException.class, ex -> Mono.empty());
//    }


    public void ping() {
        try{
            this.webClient.post()
                    .uri(urlMail + "/ping")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).subscribe();
        } catch (Exception ignored) {
        }
    }

}
