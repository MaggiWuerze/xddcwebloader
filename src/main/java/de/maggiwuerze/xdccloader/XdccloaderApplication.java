package de.maggiwuerze.xdccloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class XdccloaderApplication {

    public static void main(String[] args) {

        //creating download folder in userhome
        String path = System.getProperty("user.home") + File.separator + "downloads";
        File customDir = new File(path);

        if (customDir.exists()) {
            System.out.println("target folder " + customDir + " already exists");
        } else if (customDir.mkdirs()) {
            System.out.println("target folder " + customDir + " was created");
        } else {
            System.out.println("target folder " + customDir + " was not created");
        }


        SpringApplication.run(XdccloaderApplication.class, args);
    }

}
