package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionResponse {

    private Integer distance;

    private Integer duration;

    private Integer price;
}
