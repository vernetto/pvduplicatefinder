package org.pierre.pvduplicatefinder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinderService {
    @Autowired
    FilesCrawler filesCrawler;

    public List<DuplicateCollection> findDuplicates(String rootLocation) throws IOException {
        List<DuplicateCollection> result = new ArrayList<>();
        List<FileInfo> allFiles = filesCrawler.findAllFileInfo(rootLocation);
        System.out.println(allFiles);
        return result;
    }

}
