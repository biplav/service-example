package com.acton;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Main {
	
	public static final String PORT_ENV = System.getenv("PORT");
	public static final String PORT = PORT_ENV == null ? "8080" : PORT_ENV;
	
	 private static URI getBaseURI(int port) {
	        return UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
	    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @param uri 
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(URI uri) {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.acton package
        final ResourceConfig rc = new ResourceConfig().packages("com.acton");
        rc.register(CORSResponseFilter.class);
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(uri, rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	URI uri = getBaseURI(Integer.valueOf(PORT));
        final HttpServer server = startServer(uri);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", uri));
        while(true) {
            System.in.read();
        }
        //server.stop();
    }
}

