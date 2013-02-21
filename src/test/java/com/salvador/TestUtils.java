package com.salvador;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 21/02/13
 * Time: 03:48
 */
public class TestUtils {

    public static String getTestResourcesDirectory() {
        String directory = "";

        // maven
        if (System.getProperty("base.dir") != null) {
            directory += System.getProperty("base.dir");
        } else {
            directory += System.getProperty("user.dir");
        }

        directory += File.separator + "src"
                + File.separator + "test"
                + File.separator + "resources" + File.separator;

        return directory;
    }

}
