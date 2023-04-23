package com.plprv.PlataformaProveedores;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;



@Configuration
public class GoogleDriveConfig {
    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        Resource resource = new ClassPathResource("plataforma-proveedores-069e23af7773.json");
        InputStream inputStream = resource.getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/drive"));
        return credentials;
    }
}
