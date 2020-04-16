package org.pierre.pvduplicatefinder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FilesCrawler {

    List<FileInfo> findAllFileInfo(String rootFolder) throws IOException {
        return findAllFileInfo(Paths.get(rootFolder));
    }

    List<FileInfo> findAllFileInfo(Path rootFolder) throws IOException {
        List<FileInfo> fileInfos = new ArrayList<>();
        if (rootFolder == null || rootFolder.toFile() == null || rootFolder.toFile().listFiles() == null) {
            return fileInfos;
        }
        for (File file : rootFolder.toFile().listFiles()) {
            Path thisPath = file.toPath();
            if (file.isDirectory() && ! (file.getPath().equals(rootFolder.toFile().getPath()))) {
                findAllFileInfo(thisPath);
            }
            else {
                try {
                    FileChannel fileChannel = FileChannel.open(thisPath);
                    FileInfo fileInfo = FileInfo.builder().path(thisPath).size(fileChannel.size()).time(Files.getLastModifiedTime(thisPath)).build();
                    fileInfos.add(fileInfo);
                } catch (IOException e) {
                    log.error("error", e);
                }

            }
        }

        return fileInfos;
    }


}
