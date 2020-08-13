package jpp.infinityloop.impexp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

public class FileExchanger {

    public void exportFile(byte[] data, String outputFileName) {
        System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String stampAll = timestamp.toString();
        String stamp = stampAll.substring(0, 4);
        stamp = stamp + stampAll.substring(5, 7);
        stamp = stamp + stampAll.substring(8, 10);
        stamp = stamp + stampAll.substring(11, 13);
        stamp = stamp + stampAll.substring(14, 16);
        stamp = stamp + stampAll.substring(17, 19);
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(outputFileName));
                output.write(data);
            } finally {
                output.close();
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    public byte[] importFile(File f) {
        InputStream is = null;
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        long fileSize = f.length();
        if (fileSize > Integer.MAX_VALUE) {
            // file too big
        }
        byte[] data = new byte[(int) fileSize];
        int count;
        try {
            while ((count = is.read(data)) != -1) {
                os.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] result = os.toByteArray();
        return result;
    }
}
