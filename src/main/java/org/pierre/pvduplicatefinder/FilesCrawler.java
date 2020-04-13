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
import java.util.stream.Stream;

@Service
@Slf4j
public class FilesCrawler {

    List<FileInfo> findAllFileInfo(String rootFolder) throws IOException {
        return findAllFileInfo(Paths.get(rootFolder));
    }

    List<FileInfo> findAllFileInfo(Path rootFolder) throws IOException {
        List<FileInfo> fileInfos = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(rootFolder)) {
            walk.filter(Files::isDirectory).filter(Predicate.not(rootFolder::equals)).forEach(path -> {
                try {
                    List<FileInfo> result = findAllFileInfo(path);
                } catch (IOException e) {
                    log.error("error", e);
                }
            });
        }
        try (Stream<Path> walk = Files.walk(rootFolder)) {
            walk.filter(Files::isRegularFile).forEach(path -> {
                try {
                    FileChannel fileChannel = FileChannel.open(path);
                    FileInfo fileInfo = FileInfo.builder().path(path).size(fileChannel.size()).time(Files.getLastModifiedTime(path)).build();
                    fileInfos.add(fileInfo);
                } catch (IOException e) {
                    log.error("error", e);
                }
            });
        }
        return fileInfos;
    }


}
