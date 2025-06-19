package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IWhatsappDao;
import com.microcode.client.entity.mysql.Whatsapp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WhatsappServices implements WhatsappServicesI {

    private final IWhatsappDao whatsappDao;

    @Override
    public void create(Whatsapp whatsapp) {
        whatsappDao.save(whatsapp);
    }

    @Override
    public Whatsapp findWhatsappById(Integer whatsappId) {
        return whatsappDao.findWhatsappByWhatsappId(whatsappId);
    }

    @Override
    public Whatsapp findByNumberAndStatus(String whatsappNumber, String status) {
        return whatsappDao.findByWhatsappNumberAndWhatsappStatus(whatsappNumber,status);
    }


}
