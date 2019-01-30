package com.allandroidprojects.ecomsample.startup;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Storage extends Context {



    private void writeToAnyFile(String data, String localFileName) throws IOException {

        //  localFileName is with the file with extension which needs the content to be written on it.
        File dir = getFilesDir();
        File file = new File(dir, localFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(data);
        bufferedWriter.close();
    }


}
