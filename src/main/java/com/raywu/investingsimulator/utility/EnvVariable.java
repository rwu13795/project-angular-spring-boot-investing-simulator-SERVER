package com.raywu.investingsimulator.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvVariable {

    private final Environment env;

    @Autowired
    public EnvVariable(Environment env){
        this.env = env;
    }

    public String FMP_API_KEY(){
        return env.getProperty("FMP_API_KEY");
    }

    public String FMP_API_URL(){
        return env.getProperty("FMP_API_URL");
    }

    public String TOKEN_SECRET() { return env.getProperty("JWT_SECRET"); }

    public String SECURITY_CIPHER_KEY() { return env.getProperty("SECURITY_CIPHER_KEY"); }

    public String SENDGRID_API_KEY() { return env.getProperty("SENDGRID_API_KEY"); }
}
