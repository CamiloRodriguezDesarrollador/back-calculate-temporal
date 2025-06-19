package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.Whatsapp;

import java.util.List;

public interface WhatsappServicesI {

    void create(Whatsapp whatsapp);
    Whatsapp findWhatsappById(Integer whatsappId);
    Whatsapp findByNumberAndStatus(String whatsappNumber, String status);

}
