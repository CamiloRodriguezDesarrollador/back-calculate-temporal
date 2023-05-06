package com.plprv.PlataformaProveedores.service;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.FileList;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IProveedorDocArchivoServices {

    public String crearCarpetaProveedor(String carpetaPadre, String idCarpetaProveedor  ) throws IOException, GeneralSecurityException;

    public boolean verificarCarpetaProveedor(String carpetaPadre, String idCarpetaProveedor ) throws IOException, GeneralSecurityException;

    public FileList verificarCarpetaPeriodo(String idCarpetaProveedor, String nombreCarpetaPeriodo ) throws IOException, GeneralSecurityException;

    public FileList verificarDocumentoDoble(String nombreCarpetaPeriodo, String nombreArchivo ) throws IOException, GeneralSecurityException;

    public String cargarArchivo(String carpetaPeriodo, String nombreArchivo , MultipartFile file ) throws IOException, GeneralSecurityException;

    public Boolean compararArchivos(MultipartFile file, String archivoId ) throws IOException, GeneralSecurityException;

    public String crearCarpetaPeriodo(String carpetaProveedor, String carpetaPeriodo ) throws IOException, GeneralSecurityException;

    public byte[] descargarArchivo (String idArchivo) throws IOException, GeneralSecurityException;

    public void  borrarArchivo (String idArchivo) throws IOException, GeneralSecurityException;

    public void  guardarCorreoPeriodo (ByteArrayContent archivo, String nombreArchivo, String nombreCarpeta) throws IOException, GeneralSecurityException;

}
