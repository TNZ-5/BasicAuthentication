package com.serverlet;

import com.logicbig.example.App;
import com.logicbig.example.RequiresAuthentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class BasicAuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the header is present and starts with "Basic "
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            // Extract the Base64-encoded username and password from the header
            String base64Credentials = authorizationHeader.substring(AUTHORIZATION_HEADER_PREFIX.length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);

            // Split the credentials into username and password
            StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();

            // Perform authentication logic here (e.g., check against a user database)
            boolean isAuthenticated = performAuthentication(username, password);

            // If authentication fails, send a 401 Unauthorized response
            if (!isAuthenticated) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                        .build());
            }
        } else {
            // No Authorization header provided, send a 401 Unauthorized response
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                    .build());
        }
    }

    private boolean performAuthentication(String username, String password) {

        try{

            if(App.authenticateUserFromDatabase(username,password))
                return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
}

//
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.ResourceInfo;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.UriInfo;
//import javax.ws.rs.ext.Provider;
//import java.lang.reflect.Method;
//
//@Provider
//public class BasicAuthenticationFilter implements ContainerRequestFilter {
//    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
//
//    @Context
//    private ResourceInfo resourceInfo;
//
//    @Context
//    private UriInfo uriInfo;
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) {
//        Class<?> resourceClass = resourceInfo.getResourceClass();
//        Method resourceMethod = resourceInfo.getResourceMethod();
//        if (isAuthenticationRequired(resourceClass, resourceMethod)) {
//            // Perform authentication logic here
//            if (!isAuthenticated(requestContext)) {
//                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//            }
//            else{
//                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
//                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
//                    .build());
//            }
//        }
//    }
//
//    private boolean isAuthenticationRequired(Class<?> resourceClass, Method resourceMethod) {
//        // Check if the resource class or method is annotated with RequiresAuthentication
//        return resourceClass.isAnnotationPresent(RequiresAuthentication.class)
//                || resourceMethod.isAnnotationPresent(RequiresAuthentication.class);
//    }
//
//    private boolean isAuthenticated(ContainerRequestContext requestContext) {
//         //Get the Authorization header from the request
//        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//
//        // Check if the header is present and starts with "Basic "
//        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
//            // Extract the Base64-encoded username and password from the header
//            String base64Credentials = authorizationHeader.substring(AUTHORIZATION_HEADER_PREFIX.length());
//            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
//
//            // Split the credentials into username and password
//            StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
//            String username = tokenizer.nextToken();
//            String password = tokenizer.nextToken();
//
//            boolean isAuth = performAuthentication(username, password);
//
//            return isAuth;
//        }
//        return false;
//    }
//
//    private boolean performAuthentication(String username, String password) {
//
//        try{
//
//            if(App.authenticateUserFromDatabase(username,password))
//                return true;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//}
