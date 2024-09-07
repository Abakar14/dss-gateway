package com.bytmasoft;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {


    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
       String path="/student/dss/api/v1/**";

        path = path.substring(0, path.lastIndexOf("/")+1);

    }
}
