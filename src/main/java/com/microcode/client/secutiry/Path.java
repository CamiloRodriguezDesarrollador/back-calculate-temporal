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
    private ArrayList<Path> myRoutesOpen = new ArrayList<>(

    );

    public Path(String url, List<String> authorized) {
        this.url = url;
        this.authorized = authorized;
    }

    public Path(){}

    @Value("${aut.console.platform}")
    public String autConsolePlatform;
    @Value("${aut.provider.super}")
    public String autProviderSuper;

    @PostConstruct
    private void init() {
        myRoutesProtected.add(new Path("/api/client/allInformation", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/dataTable", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/quantity", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/delete", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/active", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/findFilter", Collections.singletonList(autConsolePlatform)));
        myRoutesOpen.add(new Path("/api/client/findClientForCode", null));
        myRoutesProtected.add(new Path("/api/client/findForId", Collections.singletonList(autConsolePlatform)));
        myRoutesOpen.add(new Path("/api/client/findDataPrincipal", null));
        myRoutesOpen.add(new Path("/api/client/findDataProfile", null));
        myRoutesOpen.add(new Path("/api/client/findMyClient", null));
        myRoutesProtected.add(new Path("/api/client/create", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/updatedFolder", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/updatedFolderInternal", Collections.singletonList(autConsolePlatform)));
        myRoutesProtected.add(new Path("/api/client/updated", Arrays.asList(autConsolePlatform,autProviderSuper) ));
        myRoutesProtected.add(new Path("/api/client", Collections.singletonList(autConsolePlatform)));
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
