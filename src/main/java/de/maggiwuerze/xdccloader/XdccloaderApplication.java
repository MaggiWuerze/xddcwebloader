package de.maggiwuerze.xdccloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class XdccloaderApplication {

    public static void main(String[] args) {

        //creating download folder if necessary
//        String path = File.separator + "opt" + File.separator + "xdcc" + File.separator + "data";
        String path = File.separator + "home" + File.separator + "dap" + File.separator + "download";
        File customDir = new File(path);

        if (customDir.exists()) {
            System.out.println("download folder exists in " + path);
        } else if (customDir.mkdirs()) {
            System.out.println("download folder was created in " + path);
        } else {
            throw new RuntimeException("target folder" + customDir + "could not created");
        }
        SpringApplication.run(XdccloaderApplication.class, args);

    }

}
