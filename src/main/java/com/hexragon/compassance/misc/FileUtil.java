package com.hexragon.compassance.misc;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileUtil
{
    /**
     * Write the InputStream to the file parameter.
     * Not a method created by me.
     *
     * @param in   Data stream.
     * @param file File to write to.
     */
    public static void write(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static LinkedList<String> fileToList(File file)
    {
        LinkedList<String> arr = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String s;
            while ((s = br.readLine()) != null)
            {
                arr.add(s);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return arr;
    }

    public static void listToFile(List<String> arr, File file)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false)))
        {
            for (String s : arr)
            {
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}