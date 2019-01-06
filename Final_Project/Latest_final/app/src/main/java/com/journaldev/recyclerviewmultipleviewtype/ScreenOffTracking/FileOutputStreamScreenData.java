package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileOutputStreamScreenData
{

    public static void TestWrite(String content) {
        //store in the Info.text file
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/ScreenOff_Info.txt");
        //use output stream to output data file
        java.io.FileOutputStream fos = null;
        try {
            //The record text file will not be replaced by the next one.
            fos = new java.io.FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes1 = content.getBytes();

            fos.write(contentInBytes1);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

