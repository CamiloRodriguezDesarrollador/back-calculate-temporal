package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.Whatsapp;
import org.springframework.data.repository.CrudRepository;

public interface IWhatsappDao extends CrudRepository<Whatsapp, Long> {

    Whatsapp findWhatsappByWhatsappId(Integer whatsappId);

    Whatsapp findByWhatsappNumberAndWhatsappStatus(String whatsappNumber, String status);


}
