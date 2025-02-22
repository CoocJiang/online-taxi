package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiDriverPointRequest {

    public Long carId;

    private PointDTO[] points;
}
