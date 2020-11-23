package tv.strohi.stfu.playlistservice.update.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateExtractor {
    private final String zipPath;
    private final File targetDir;

    public UpdateExtractor(String zipPath, String targetDir) {
        this.zipPath = zipPath;
        this.targetDir = new File(targetDir);
    }

    public String extractSingle(Condition condition) {
        return Arrays.stream(extractAll(condition, 1)).findFirst().orElse(null);
    }

    public String[] extractAll(Condition condition, int maxCount) {
        List<String> extracted = new ArrayList<>();

        if (maxCount < 1) {
            maxCount = Integer.MAX_VALUE;
        }

        try {
            int count = 0;
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (condition.applies(zipEntry.getName())) {
                    File newFile = newFile(targetDir, zipEntry);
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    extracted.add(newFile.getPath());

                    count ++;
                    if (count == maxCount) {
                        break;
                    }
                }

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            // nichts tun, geht halt net
        }

        return extracted.toArray(String[]::new);
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
