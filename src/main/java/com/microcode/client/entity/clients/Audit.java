package com.microcode.client.entity.clients;

import java.io.Serializable;

public class Audit implements Serializable {
    public String endPoint;
    public String entity;
    public String content;
    public String audUser;
    public String method;
    public String audIp;
    public String audUserAgent;

    public Audit() {
    }


    public Audit(String endPoint, String entity, String content, String audUser, String method, String audIp, String audUserAgent) {
        this.endPoint = endPoint;
        this.entity = entity;
        this.content = content;
        this.audUser = audUser;
        this.method = method;
        this.audIp = audIp;
        this.audUserAgent = audUserAgent;
    }

}

