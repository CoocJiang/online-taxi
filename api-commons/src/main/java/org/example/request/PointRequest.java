package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRequest {

    private String tid;

    private String trid;

    private PointDTO[] points;

}
