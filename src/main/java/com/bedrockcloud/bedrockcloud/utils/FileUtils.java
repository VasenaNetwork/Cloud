package com.bedrockcloud.bedrockcloud.utils;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.utils.config.ConfigUtils;

import java.io.*;

public class FileUtils {

    public static int getFreeNumber(final String path) {
        boolean found = false;
        int i = 1;
        while (!found) {
            final File file = new File(path + Utils.getServiceSeparator() + i);
            if (!file.exists()) {
                found = true;
                --i;
            }
            ++i;
        }
        return i;
    }

    public static void copy(final File src, final File dest) {
        try {
            if (src.isDirectory()) {
                if (!dest.exists()) {
                    dest.mkdir();
                }
                final String[] list;
                final String[] files = list = src.list();
                for (final String file : list) {
                    final File srcFile = new File(src, file);
                    final File destFile = new File(dest, file);
                    copy(srcFile, destFile);
                }
            } else {
                InputStream in = null;
                try {
                    in = new FileInputStream(src);
                    final OutputStream out = new FileOutputStream(dest);
                    final byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    Cloud.getLogger().exception(e);
                }
            }
        } catch (Exception ignored) {}
    }

    public static void deleteServer(final File file, final String serverName, boolean isStatic) {
        try {
            final File crashfile = new File("./temp/" + serverName + "/crashdumps/");
            final File dest_lib = new File("./archive/crashdumps/" + serverName + "/");
            if (!dest_lib.exists()) dest_lib.mkdirs();

            if (crashfile.exists()) {
                FileUtils.copy(crashfile, dest_lib);
                ConfigUtils.copyFile(crashfile, dest_lib);
            }
        } catch (IOException e) {
            Cloud.getLogger().exception(e);
        }

        if (file != null) {
            try {
                if (!isStatic) {
                    if (file.isDirectory()) {
                        final String[] fileList = file.list();
                        if (fileList != null) {
                            for (final String fileName : fileList) {
                                final String fullPath = file.getPath() + "/" + fileName;
                                final File fileOrFolder = new File(fullPath);
                                delete(fileOrFolder);
                            }
                        }

                        delete(file);
                    } else {
                        file.delete();
                    }
                }
            } catch (NullPointerException e) {
                Cloud.getLogger().exception(e);
            }
        }
    }

    public static void clearTemplatesFolder(final String templateName, boolean isStatic) {
        File tempFolder = new File("./temp/");
        File[] matchingFolders = tempFolder.listFiles((dir, name) -> name.startsWith(templateName));

        if (matchingFolders != null) {
            for (File folder : matchingFolders) {
                if (!isStatic && folder != null && folder.isDirectory()) {
                    delete(folder);
                }
            }
        }
    }

    public static void delete(final File file) {
        try {
            if (file.isDirectory()) {
                final String[] fileList = file.list();
                if (fileList.length == 0) {
                    file.delete();
                } else {
                    for (final String fileName : fileList) {
                        final String fullPath = file.getPath() + "/" + fileName;
                        final File fileOrFolder = new File(fullPath);
                        delete(fileOrFolder);
                    }
                    delete(file);
                }
            } else {
                file.delete();
            }
        } catch (NullPointerException ignored){}
    }
}
