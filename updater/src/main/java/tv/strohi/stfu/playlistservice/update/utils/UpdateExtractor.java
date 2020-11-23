package tv.strohi.stfu.playlistservice.update.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final Logger logger = LogManager.getLogger(UpdateExtractor.class.getCanonicalName());

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

        logger.info("extracting up to {} files...", maxCount);

        try {
            int count = 0;
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (condition.applies(zipEntry.getName())) {
                    File newFile = newFile(targetDir, zipEntry);
                    logger.info("extracting file '{}' to path '{}'", zipEntry.getName(), newFile.getPath());
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    extracted.add(newFile.getPath());

                    count ++;
                    if (count == maxCount) {
                        logger.info("max count of {} reached, stopping extraction", maxCount);
                        break;
                    }
                } else {
                    logger.info("skipping file '{}' because condition did not apply", zipEntry.getName());
                }

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            logger.error("could not extract files...");
            logger.error("error message: {}", e.getMessage());
            logger.error(e);
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
