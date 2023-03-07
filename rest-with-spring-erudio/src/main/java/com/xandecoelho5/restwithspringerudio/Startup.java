package com.xandecoelho5.restwithspringerudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

@SpringBootApplication
public class Startup {

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);

//        Map<String, PasswordEncoder> encoders = new HashMap<>();
//        var pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, PBKDF2WithHmacSHA256);
//        encoders.put("pbkdf2", pbkdf2Encoder);
//        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
//        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
//
//        System.out.println(passwordEncoder.encode("admin123"));
//        System.out.println(passwordEncoder.encode("admin234"));

//        {pbkdf2}7ca6b403928c2d7434b261bfd499efe7aaa92bd0a3fb2c2a19735750ae6b2bc709084bb86ff98e63
//        {pbkdf2}544e6e5c71438b96070abaf245f807aa37a31ef1a31f30f98614833b4c27ba092da604be44126dc9
    }

}
