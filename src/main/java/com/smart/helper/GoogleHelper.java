package com.smart.helper;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GoogleHelper {

    public static String USER_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static String SCOPE = "https://www.googleapis.com/auth/userinfo.email+"
            + "https://www.googleapis.com/auth/userinfo.profile";

//    public static HttpTransport HTTP_TRANSPORT;

    private String client_id= "1020058965393-3nfgaabpc4ptmtinphhqkae7lnju705i.apps.googleusercontent.com";
    @Bean
    public String redirectUri(){

        return "https://accounts.google.com/o/oauth2/v2/auth?"+
                "scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly&"+
                "prompt=consent&"+
                "access_type=offline&"+
                "include_granted_scopes=true&"+
                "response_type=code"+
                "&state=state_parameter_passthrough_value&"+
                "redirect_uri=http://localhost:8080/oauthcallback/code&client_id="+client_id;
    }
}
