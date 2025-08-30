package com.microcode.client.secutiry;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Setter
@Service
public class Path {

    private String url;
    private List<String> authorized;
    private ArrayList<Path> myRoutesProtected = new ArrayList<>();
    private ArrayList<Path> myRoutesOpen = new ArrayList<>();

    public Path(String url, List<String> authorized) {
        this.url = url;
        this.authorized = authorized;
    }

    public Path(){}

    @Value("${aut.console.platform}")
    public String autConsolePlatform;


    @PostConstruct
    private void init() {

        myRoutesOpen.add(new Path("/api/app", null));
        myRoutesOpen.add(new Path("/api/chat/company", null));
        myRoutesOpen.add(new Path("/api/chat/action", null));

        myRoutesOpen.add(new Path("/api/chat/wp/status", null));
        myRoutesOpen.add(new Path("/api/chat/wp", null));

        myRoutesProtected.add(new Path("/api/chat/action", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/action/dataTable", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/action/quantity", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/action/actions", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/action/create", Collections.singletonList(autConsolePlatform)));

        myRoutesProtected.add(new Path("/api/chat/principal-data", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/principal-data/dataTable", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/principal-data/quantity", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/principal-data/actions", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/principal-data/create", Collections.singletonList(autConsolePlatform)));

        myRoutesProtected.add(new Path("/api/chat/chats/active/chats", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/chats/sendMessageChatId", Collections.singletonList(autConsolePlatform)));

        myRoutesProtected.add(new Path("/api/chat/quantity/dataTable", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/quantity/quantity", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/quantity/delete", Collections.singletonList(autConsolePlatform)));

        myRoutesProtected.add(new Path("/api/chat/register/dataTable", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/chat/register/quantity", Collections.singletonList(autConsolePlatform)));


    }

    public List<String> getAuthorizedForUrl(String url) {
        Optional<Path> pathOptional = myRoutesProtected.stream()
                .filter(path -> path.getUrl().equals(url))
                .findFirst();

        return pathOptional.map(path -> new ArrayList<>(path.getAuthorized()))
                .orElse(null);
    }

    public Boolean getOpenForUrl(String url) {
        return myRoutesOpen.stream()
                .anyMatch(path -> path.getUrl().equals(url));
    }




}
