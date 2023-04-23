package com.plprv.PlataformaProveedores.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.services.drive.model.File;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import com.google.auth.oauth2.GoogleCredentials;

import javax.imageio.ImageIO;
import java.util.List;

@Service
public class ProveedorDocArchivoServices implements IProveedorDocArchivoServices {

    @Autowired
    private GoogleCredentials googleCredentials;

    @Override
    public String crearCarpetaProveedor(String carpetaPadre, String nombreCarpetaProveedor) throws IOException, GeneralSecurityException {

        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        File fileMetadata = new File();
        fileMetadata.setName(nombreCarpetaProveedor);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        List<String> parents = Arrays.asList(carpetaPadre);
        fileMetadata.setParents(parents);
        File folder = service.files().create(fileMetadata).setFields("id").execute();
        return  folder.getId();
    }

    @Override
    public boolean verificarCarpetaProveedor(String carpetaPadre, String idCarpetaProveedor) throws IOException, GeneralSecurityException {

        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        String query = "'" + carpetaPadre + "' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'";
        FileList result = service.files().list().setQ(query).setFields("nextPageToken, files(id, name , parents, owners)").execute();
        List<File> files = result.getFiles();
        boolean encontrado = files.stream().anyMatch(fileD -> fileD.getId().equals(idCarpetaProveedor));
        return encontrado;
    }

    @Override
    public FileList verificarCarpetaPeriodo(String idCarpetaProveedor, String nombreCarpetaPeriodo) throws IOException, GeneralSecurityException {
        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();


        String query = "mimeType='application/vnd.google-apps.folder' and trashed = false and name = '" + nombreCarpetaPeriodo + "' and '" + idCarpetaProveedor + "' in parents";
        FileList result = service.files().list().setQ(query).setSpaces("drive").setFields("nextPageToken, files(id, name)").execute();

        return result;
    }

    @Override
    public String crearCarpetaPeriodo(String carpetaProveedor, String carpetaPeriodo ) throws IOException, GeneralSecurityException {

        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        File fileMetadata = new File();
        fileMetadata.setName(carpetaPeriodo);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        List<String> parents = Arrays.asList(carpetaProveedor);
        fileMetadata.setParents(parents);

        File folder = service.files().create(fileMetadata).setFields("id").execute();
        return  folder.getId();
    }

    @Override
    public byte[] descargarArchivo(String idArchivo) throws IOException, GeneralSecurityException {
        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(idArchivo).executeMediaAndDownloadTo(outputStream);
        return outputStream.toByteArray();

    }

    @Override
    public void borrarArchivo(String idArchivo) throws IOException, GeneralSecurityException {
        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();
        try {
            service.files().delete(idArchivo).execute();
        }catch (GoogleJsonResponseException e){
        }
    }

    @Override
    public FileList verificarDocumentoDoble(String nombreCarpetaPeriodo, String nombreArchivo) throws IOException, GeneralSecurityException {

        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        String queryD = "trashed = false and name = '" + nombreArchivo + "' and '" + nombreCarpetaPeriodo + "' in parents";
        FileList resultD = service.files().list().setQ(queryD).setSpaces("drive").setFields("nextPageToken, files(id, name)").execute();
        return resultD;
    }

    @Override
    public String cargarArchivo(String carpetaPeriodo, String nombreArchivo, MultipartFile file) throws IOException, GeneralSecurityException {

        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        byte[] fileBytes = file.getBytes();
        File fileMetadata = new File();
        fileMetadata.setName(nombreArchivo);
        fileMetadata.setParents(Collections.singletonList(carpetaPeriodo));
        ByteArrayContent mediaContent = new ByteArrayContent(file.getContentType(), fileBytes);
        File archivo = service.files().create(fileMetadata, mediaContent).setFields("id").execute();

        return archivo.getId();
    }

    @Override
    public Boolean compararArchivos(MultipartFile file, String archivoId) throws IOException, GeneralSecurityException {

        Drive service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("plataformaProveedores").build();

        Drive.Files.Get request = service.files().get(archivoId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        request.executeMedia().download(outputStream);

        String contenido2 = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        byte[] contenido1 = file.getBytes();

        String strContenido1 = new String(contenido1);
        String strContenido2 = new String(contenido2);

        if (strContenido1.equals(strContenido2)) {
            return true;
        }else {
            return false;
        }
    }
}
