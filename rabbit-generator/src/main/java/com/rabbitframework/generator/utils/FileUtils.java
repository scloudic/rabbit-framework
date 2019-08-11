package com.rabbitframework.generator.utils;

import com.rabbitframework.generator.exceptions.GeneratorException;

import java.io.*;
import java.util.StringTokenizer;

public class FileUtils {
    public static void writeFile(File file, String content, String fileEncoding) throws GeneratorException {
        BufferedWriter bw = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file, false);
            if (fileEncoding == null) {
                osw = new OutputStreamWriter(fos);
            } else {
                osw = new OutputStreamWriter(fos, fileEncoding);
            }
            bw = new BufferedWriter(osw);
            bw.write(content);
        } catch (IOException e) {
            throw new GeneratorException(e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e) {

            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (Exception e) {
            }

        }
    }

    public static File getDirectory(String targetProject, String targetPackage)
            throws GeneratorException {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            project.mkdirs();
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new GeneratorException("Cannot create directory" +
                        directory.getAbsolutePath());
            }
        }

        return directory;
    }
}
