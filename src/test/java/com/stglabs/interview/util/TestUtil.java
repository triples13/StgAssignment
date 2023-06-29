package com.stglabs.interview.service.util;

import java.io.File;
import java.nio.file.Paths;

public class TestUtil {

    public static File readFile(String path ){
        return Paths.get("src/test/resources/"+path).toFile();
    }
}
