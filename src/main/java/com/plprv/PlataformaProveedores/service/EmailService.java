package com.plprv.PlataformaProveedores.service;

import com.google.api.client.http.ByteArrayContent;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;



@Service
@Transactional
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private IProveedorDocArchivoServices proveedorDocArchivoService;

    @Value("${spring.mail.username")
    private String email;


    public void sendListEmail(String emailTo, MultipartFile file, String perNombre , String perFechaEvaluacion, String nombreCarpeta) throws IOException {

        String texto = "<html>" +
            "<head>" +
            "<style>" +
            "h1 {color: #f3af32; font-size: 14px; text-align: center;}" +
            "p {color: #333333; font-size: 13px;}" +
            "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}" +
            "#datosCorreo p{margin: 0px; font-weight: bolder;}" +
            "</style>" +
            "</head>" +
            "<body>" +

            "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
            "<h1>Evaluación de proveedor <strong> " + perNombre + " </strong> el cual evalua el periodo <strong> "+ perFechaEvaluacion +" </strong> Resultados del período</h1>"+
            "<p>Estimado proveedor,</p>"+
            "<p>De acuerdo a lo establecido en nuestros procedimientos internos, compartimos la reevaluación de proveedores realizada con base en la gestión del periodo <strong>"+perFechaEvaluacion+"</strong>, igualmente las observaciones obtenidas como parte del seguimiento hecho por los procesos.</p>"+
            "<p>Adjunto encontrará el informe completo de la evaluación en formato PDF. Si tiene alguna pregunta o comentario sobre los resultados de la evaluación, no dude en ponerse en contacto con nosotros.</p>"+
            "<p>Gracias nuevamente por su compromiso con la excelencia.</p>"+
            "<div id='datosCorreo'>"+
            "<p>Atentamente,</p>"+
            "<p>Equipo de Compras </p>"+
            "<p>Activos SAS </p>"+
            "</div>"+
            "</body>" +
            "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject("Evaluacion Desempeño  ACTIVOS SAS - Periodo :" + perFechaEvaluacion);
            helper.setText(texto,true);
            helper.addAttachment("Evaluacion",file, "application/pdf");
            javaMailSender.send(message);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);

            ByteArrayContent mediaContent = new ByteArrayContent("message/rfc822", outputStream.toByteArray());
            proveedorDocArchivoService.guardarCorreoPeriodo(mediaContent, "Correo evaluacion " + perFechaEvaluacion, nombreCarpeta);

        } catch (Exception e) {
        }
    }

    public void sendListEmailNoIniciado(List<String> correosProveedor , String perFechaEvaluacion) throws IOException {

        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "h1 {color: #f3af32; font-size: 14px; text-align: center; text-transform: capitalize;}" +
                "p {color: #333333; font-size: 13px;}" +
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}" +
                "#datosCorreo p{margin: 0px; font-weight: bolder;}" +
                "</style>" +
                "</head>" +
                "<body>" +

                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Solicitud Documental <strong> </strong> periodo: <strong> "+ perFechaEvaluacion +" </strong></h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>De acuerdo a lo establecido en nuestros procedimientos internos, informamos que reportan como <strong>PENDIENTES - SIN INICIAR</strong> en " +
                " el cargue documental de nuestra organizacion, por tal motivo requerimos de su colaboración con el ingreso de la documentación " +
                " en la siguiente plataforma." +
                " El periodo evaluado es el : <strong>"+perFechaEvaluacion+"</strong>.</p>"+
                "<p>Link de la plataforma :</p>"+
                "<a href='http://localhost:5173/'> Link plataforma </a>"+
                "<p>Si tiene alguna pregunta o comentario sobre los resultados de la evaluación, no dude en ponerse en contacto con nosotros.</p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        for (String correo : correosProveedor) {
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                helper.setFrom(email);
                helper.setTo(correo);
                helper.setCc("cgonzalez@activos.com.co");
                helper.setSubject("Solicitud Documentación ACTIVOS SAS - Periodo :" + perFechaEvaluacion);
                helper.setText(texto,true);
                javaMailSender.send(message);
            } catch (Exception e) {
            }
        }
    }

    public void sendListEmailIncompleto(List<String> correosProveedor , String perFechaEvaluacion) throws IOException {

        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "h1 {color: #f3af32; font-size: 14px; text-align: center; text-transform: capitalize;}" +
                "p {color: #333333; font-size: 13px;}" +
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}" +
                "#datosCorreo p{margin: 0px; font-weight: bolder;}" +
                "</style>" +
                "</head>" +
                "<body>" +

                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Solicitud Documental <strong> </strong> periodo: <strong> "+ perFechaEvaluacion +" </strong></h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>De acuerdo a lo establecido en nuestros procedimientos internos, informamos que reportan como <strong>PENDIENTES - INCOMPLETO</strong> en " +
                " el cargue documental de nuestra organizacion, por tal motivo requerimos de su colaboración con el ingreso de la documentación " +
                " en la siguiente plataforma." +
                " El periodo evaluado es el : <strong>"+perFechaEvaluacion+"</strong>.</p>"+
                "<p>Link de la plataforma :</p>"+
                "<a href='http://localhost:5173/'> Link plataforma </a>"+
                "<p>Si tiene alguna pregunta o comentario sobre los resultados de la evaluación, no dude en ponerse en contacto con nosotros.</p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        for (String correo : correosProveedor) {
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                helper.setFrom(email);
                helper.setTo(correo);
                helper.setSubject("Solicitud Documentación ACTIVOS SAS - Periodo :" + perFechaEvaluacion);
                helper.setText(texto,true);
                javaMailSender.send(message);
            } catch (Exception e) {

            }
        }
    }

    public void sendListEmailRegistro(String correo , String nombreProveedor, String autCodigoCorreo) throws IOException {

        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "*{width:500px}"+
                "h1 {color: #f3af32; font-size: 14px; text-align: center; text-transform: capitalize;}"+
                "h6 {font-size: 11px; text-align: center;}"+
                "p {color: #333333; font-size: 13px;}"+
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}"+
                "#datosCorreo p{margin: 0px; font-weight: bolder;}"+
                "#codigo{letter-spacing: 10px; }"+
                "#codigo h2{font-size: 20px; text-align: center; border: 1px solid grey; border-radius: 10px; max-width: 250px; width: 100%; font-weight: bolder;  height: 30px;}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Solicitud de Ingreso - Plataforma Proveedores ACTIVOS SAS</h1>"+
                "<h1>Bienvenido, Srs <strong> "+ nombreProveedor +" </strong></h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>De acuerdo al registro solicitado, a continuación compartimos el siguiente código de <strong>verificación.</strong></p>"+
                "<div id='codigo'>"+
                "<h2>"+ autCodigoCorreo +"</h2>"+
                "</div>"+
                "<p>Favor ingresar al siguiente link para verificar tu identidad - <a href='http://localhost:5173/verificacion'> Link </a></p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "<h6>Si no has solicitado este correo electrónico, por favor ignóralo. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte</h6>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                helper.setFrom(email);
                helper.setTo(correo);
                helper.setSubject("Código Verificación Plataforma Activos SAS - " + nombreProveedor);
                helper.setText(texto,true);
                javaMailSender.send(message);
            } catch (Exception e) {
        }
    }

    public void sendListEmailConfirmacion(String correo , String nombreProveedor) throws IOException {

        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "h1 {color: #f3af32; font-size: 14px; text-align: center;}"+
                "h6 {font-size: 11px; text-align: center;}"+
                "p {color: #333333; font-size: 13px;}"+
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}"+
                "#datosCorreo p{margin: 0px; font-weight: bolder;}"+
                "#codigo{letter-spacing: 10px; }"+
                "#codigo h2{font-size: 20px; text-align: center; border: 1px solid grey; border-radius: 10px; max-width: 250px; width: 100%; font-weight: bolder;  height: 30px;}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Confirmación de Ingreso - Plataforma Proveedores ACTIVOS SAS</h1>"+
                "<h1>Bienvenido, Srs <strong> "+ nombreProveedor +" </strong></h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>De acuerdo a registro solicitado,  confirmamos verificación del correo."+
                "<p>Una vez el Administrador de la plataforma ACTIVOS haya aprobado su ingreso, porfavor acceder por el siguiente link - <a href='http://localhost:5173/'> Link </a></p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "<h6>Si no has solicitado este correo electrónico, por favor ignóralo. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte</h6>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(email);
            helper.setTo(correo);
            helper.setSubject("Confirmación Correo Plataforma Activos SAS - " + nombreProveedor);
            helper.setText(texto,true);
            javaMailSender.send(message);
        } catch (Exception e) {

        }
    }

    public void sendListEmailIngresoCorrecto(String correo , String nombreProveedor) throws IOException {

        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "h1 {color: #f3af32; font-size: 14px; text-align: center;}"+
                "h6 {font-size: 11px; text-align: center;}"+
                "p {color: #333333; font-size: 13px;}"+
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}"+
                "#datosCorreo p{margin: 0px; font-weight: bolder;}"+
                "#codigo{letter-spacing: 10px; }"+
                "#codigo h2{font-size: 20px; text-align: center; border: 1px solid grey; border-radius: 10px; max-width: 250px; width: 100%; font-weight: bolder;  height: 30px;}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Confirmación de Ingreso- Plataforma Proveedores ACTIVOS SAS</h1>"+
                "<h1>Bienvenido, Srs <strong> "+ nombreProveedor +" </strong></h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>De acuerdo a registro solicitado,  confirmamos <strong> VERIFICACIÓN </strong> de ingreso por parte del administrador."+
                "<p>Por favor acceder por el siguiente link con las credenciales registradas- <a href='http://localhost:5173/'> Link </a></p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "<h6>Si no has solicitado este correo electrónico, por favor ignóralo. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte</h6>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(email);
            helper.setTo(correo);
            helper.setSubject("Confirmación Correo Plataforma Activos SAS - " + nombreProveedor);
            helper.setText(texto,true);
            javaMailSender.send(message);
        } catch (Exception e) {

        }
    }

    public void sendCorreoRecuperar(String correo , String autCodigoCorreo) throws IOException {
        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "*{width:500px}"+
                "h1 {color: #f3af32; font-size: 14px; text-align: center; text-transform: capitalize;}"+
                "h6 {font-size: 11px; text-align: center;}"+
                "p {color: #333333; font-size: 13px;}"+
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}"+
                "#datosCorreo p{margin: 0px; font-weight: bolder;}"+
                "#codigo{letter-spacing: 10px; }"+
                "#codigo h2{font-size: 20px; text-align: center; border: 1px solid grey; border-radius: 10px; max-width: 250px; width: 100%; font-weight: bolder;  height: 30px;}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Solicitud recuperación contrasena - Plataforma Proveedores ACTIVOS SAS</h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>Favor dirigirse al siguiente link.</p>"+
                "<p><a href='http://localhost:5173/verificacionContrasena?id="+autCodigoCorreo +"'> Link </a></p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "<h6>Si no has solicitado este correo electrónico, por favor ignóralo. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte</h6>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(email);
            helper.setTo(correo);
            helper.setSubject("Recuperación Contrasena Plataforma Activos SAS");
            helper.setText(texto,true);
            javaMailSender.send(message);
        } catch (Exception e) {

        }
    }

    public void sendListEmailCambioCorreo(String correo , String autCodigoCorreo) throws IOException {

        String texto = "<html>" +
                "<head>" +
                "<style>" +
                "*{width:500px}"+
                "h1 {color: #f3af32; font-size: 14px; text-align: center; text-transform: capitalize;}"+
                "h6 {font-size: 11px; text-align: center;}"+
                "p {color: #333333; font-size: 13px;}"+
                "img {display: block; margin: 0 auto; width: 100px; height: 30px; margin-bottom: 5px;margin-top: 5px;}"+
                "#datosCorreo p{margin: 0px; font-weight: bolder;}"+
                "#codigo{letter-spacing: 10px; }"+
                "#codigo h2{font-size: 20px; text-align: center; border: 1px solid grey; border-radius: 10px; max-width: 250px; width: 100%; font-weight: bolder;  height: 30px;}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<img src='https://drive.google.com/uc?id=14kQNKo-7FYIGnfyd4EUoXh6to5Rduuth' alt='Activos SAS'>" +
                "<h1>Solicitud de Cambio de Correo - Plataforma Proveedores ACTIVOS SAS</h1>"+
                "<p>Estimado proveedor,</p>"+
                "<p>De acuerdo al registro solicitado, a continuación compartimos el siguiente código de <strong>verificación.</strong></p>"+
                "<div id='codigo'>"+
                "<h2>"+ autCodigoCorreo +"</h2>"+
                "</div>"+
                "<p>Favor ingresar el código en la plataforma.</p>"+
                "<p>Gracias por su atención.</p>"+
                "<div id='datosCorreo'>"+
                "<p>Atentamente,</p>"+
                "<p>Equipo de Compras </p>"+
                "<p>Activos SAS </p>"+
                "</div>"+
                "<h6>Si no has solicitado este correo electrónico, por favor ignóralo. Si tienes alguna pregunta o inquietud, por favor ponte en contacto con nuestro equipo de soporte</h6>"+
                "</div>"+
                "</body>" +
                "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(email);
            helper.setTo(correo);
            helper.setSubject("Código Verificación Cambio Correo Plataforma Activos SAS");
            helper.setText(texto,true);
            javaMailSender.send(message);
        } catch (Exception e) {

        }
    }

}
