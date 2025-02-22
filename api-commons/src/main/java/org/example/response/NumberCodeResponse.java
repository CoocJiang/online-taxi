package org.example.response;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class NumberCodeResponse {
    private int Numbercode;

    public int getNumbercode(){
        return this.Numbercode;
    }
}

