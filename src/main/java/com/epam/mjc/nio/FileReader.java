package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileReader {

    Logger logger = Logger.getGlobal();
    public Profile getDataFromFile(File file) {
        try (RandomAccessFile aFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = aFile.getChannel()
        ) {

            long fileSize = inChannel.size();

            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            inChannel.read(buffer);
            buffer.flip();
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < fileSize; i++) {
                input.append((char) buffer.get());
            }
            String[] profile = input.toString().split(": |\r\n|\n");
            String name = null;
            int age = 0;
            String email = null;
            Long phone = 0L;
            for (int i = 0; i < profile.length - 1; i++) {
                switch (profile[i]) {
                    case "Name":
                        name = profile[i + 1];
                        break;
                    case "Age":
                        age = parseAge(profile[i + 1]);
                        break;
                    case "Email":
                        email = profile[i + 1];
                        break;
                    case  "Phone":
                        phone = parsePhone(profile[i + 1]);
                        break;
                    default:
                        break;
                }
            }
            return new Profile(name, age, email, phone);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.toString());
            return null;
        }

    }
    Long parsePhone(String phone){
        try {
            return Long.parseLong(phone);
        } catch (NumberFormatException ex){
            logger.log(Level.WARNING, ex.toString());
            return 0L;
        }
    }
    int parseAge(String age){
        try {
            return Integer.parseInt(age);
        } catch (NumberFormatException ex){
            logger.log(Level.WARNING, ex.toString());
            return 0;
        }
    }
}
