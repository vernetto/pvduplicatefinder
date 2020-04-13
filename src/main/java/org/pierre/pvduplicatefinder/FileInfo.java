package org.pierre.pvduplicatefinder;

import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;

@Data
public class FileInfo {
    File file;
    LocalDateTime time;
    long size;
    String extension;

}
