package com.microcode.client.service.mysql;

import com.microcode.client.entity.general.ContentMessage;
import com.microcode.client.entity.general.ContentResponse;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Getter
@Service
public class Salt {

    public static ContentMessage unwrapContentMessage(ContentMessage contentMessage) {
        Map<String, String> unwrappedMessages = contentMessage.getChatMessage();

        for (Map.Entry<String, String> entry : unwrappedMessages.entrySet()) {
            if(entry.getValue() != null) entry.setValue(unwrapMessage(entry.getValue()));
        }

        contentMessage.setChatMessage(unwrappedMessages);

        return contentMessage;
    }

    public static String unwrapMessage(String wrappedPassword) {
        String salt = wrappedPassword.substring(wrappedPassword.length() - 10);
        StringBuilder mixedPassword = new StringBuilder();
        for (int i = 0; i < wrappedPassword.length() - 10; i++) {
            int charCode = wrappedPassword.charAt(i);
            mixedPassword.append((char) (charCode - 1000));
        }
        int startIndex = "5AppPlIn545704g17523-1*._".length();
        int endIndex = mixedPassword.length() - salt.length();

        return mixedPassword.substring(startIndex, endIndex);
    }

    public static String wrapMessage(String password) {
        String salt = generateSalt();
        String mixedPassword = "5AppPlIn545704g17523-1*._" + password + salt;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mixedPassword.length(); i++) {
            char c = mixedPassword.charAt(i);
            result.append((char) (c + 1000));
        }
        return result + salt;
    }

    private static String generateSalt() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
        Random random = new Random();
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(saltChars.length());
            salt.append(saltChars.charAt(index));
        }

        return salt.toString();
    }

    public static ContentResponse wrapContentResponde(ContentResponse contentResponse){
        contentResponse.setActionMessage(Salt.wrapMessage(contentResponse.getActionMessage()));
        return contentResponse;
    }


}
