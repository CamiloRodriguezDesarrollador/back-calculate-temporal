package com.microcode.client.controller;

import com.microcode.client.clients.ConnectedServices;
import com.microcode.client.clients.NotifyServices;
import com.microcode.client.entity.AppSheets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@RequestMapping("/api/calculate-temporal")
@AllArgsConstructor
@RestController
public class ClientController {

    private final ConnectedServices connectedServices;
    private final NotifyServices notifyServices;

    @GetMapping("")
    public List<AppSheets> getPremisas() {
        return connectedServices.getDataAppsheets();
    }
    @PostMapping("")
    public Mono<String> setForm(@RequestBody Map<String, Object> formData) {

        String nombre = (String) formData.getOrDefault("NOMBRE", "No registrado");
        String correo = (String) formData.getOrDefault("CARGO", "Sin correo");
        String telefono = (String) formData.getOrDefault("CORREO", "Sin teléfono");
        String mensaje = (String) formData.getOrDefault("TELEFONO", "Sin observaciones");

        String chatMessage = String.format(
                "🎉 *¡Nuevo Registro desde la Calculadora!* 🎉\n\n" +
                        "Hola equipo, un nuevo contacto acaba de ingresar sus datos:\n\n" +
                        "👤 *Nombre:* %s\n" +
                        "📧 *Correo:* %s\n" +
                        "📱 *Teléfono:* %s\n" +
                        "💬 *Mensaje:* %s\n\n",
                nombre, correo, telefono, mensaje
        );

        return connectedServices.sendToAppSheet(formData)
                .doOnSuccess(appSheetResponse -> {
                    notifyServices.sendNotifyChat(chatMessage)
                            .subscribe(
                                    null,
                                    error -> log.error("Falló la notificación en background: {}", error.getMessage())
                            );
                });
    }


}
