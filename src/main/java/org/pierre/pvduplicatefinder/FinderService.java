package org.pierre.pvduplicatefinder;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinderService {
    @Autowired
    FilesCrawler filesCrawler;

    public List<DuplicateCollection> findDuplicates(String... rootLocations) throws IOException {
        List<DuplicateCollection> result = new ArrayList<>();
        List<FileInfo> allFiles = new ArrayList<>();
        for (String rootLocation : rootLocations)  {
            allFiles.addAll(filesCrawler.findAllFileInfo(rootLocation));
        }
        Collections.sort(allFiles, Comparator.comparing(FileInfo::getSize));
        allFiles.forEach(System.out::println);
        Map<Long, List<FileInfo>> allFilesGroupedBySize = allFiles.stream().collect(Collectors.groupingBy(FileInfo::getSize));
        System.out.println("=====================");
        allFilesGroupedBySize.forEach((aLong, fileInfos) -> System.out.println(aLong + " " + fileInfos));

        allFilesGroupedBySize.forEach((aLong, fileInfos) -> {
            if (aLong > 1) {
                fileInfos.forEach(fileInfo -> {
                    try {
                        fileInfo.sha2 = DigestUtils.sha256Hex(new FileInputStream(fileInfo.path.toFile()));
                    } catch (IOException e) {
                        log.error("error" , e);
                        fileInfo.sha2 = "ERROR";
                    }
                });
                Map<String, List<FileInfo>> mapWithSameSha2 = fileInfos.stream().collect(Collectors.groupingBy(FileInfo::getSha2));
                mapWithSameSha2.forEach((s, fileInfos1) -> {
                    if (fileInfos.size() >= 2) {
                        System.out.println("DUPLICATE " + fileInfos1);
                    }
                });

            }
        });

        return result;
    }

}
