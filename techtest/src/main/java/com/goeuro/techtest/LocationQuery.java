package com.goeuro.techtest;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

/**
 *
 * @author achir
 */
public class LocationQuery {

    public String request(String endpoint, String city) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(UriBuilder.fromUri(endpoint).build());
        return target.path(city).request().accept(MediaType.APPLICATION_JSON).get(String.class);
    }
}
