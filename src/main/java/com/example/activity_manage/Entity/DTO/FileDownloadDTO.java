package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDownloadDTO {
    private Long fid;
    private Long aid;
    private String fileName;
}
