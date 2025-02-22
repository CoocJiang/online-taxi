package org.example.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constrains.VehicleTypeCheck;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastPriceDTO {

    @NotBlank
    @Pattern(regexp = "^[\\-]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]\\d{1}\\.\\d{1,6}|180\\.0{1,6})$",message = "请输入正确的经度")
    private String depLongitude;
    @NotBlank
    @Pattern(regexp = "^[\\-]?([1-8]?\\d{1}\\.\\d{1,6}|90\\.\\d{1,6})$",message = "请输入正确的纬度")
    private String depLatitude;

    @NotBlank
    @Pattern(regexp = "^[\\-]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]\\d{1}\\.\\d{1,6}|180\\.0{1,6})$",message = "请输入正确的经度")
    private String destLongitude;
    @NotBlank
    @Pattern(regexp = "^[\\-]?([1-8]?\\d{1}\\.\\d{1,6}|90\\.0{1,6})$",message = "请输入正确的纬度")
    private String destLatitude;
    @NotNull
    private String cityCode;

    @VehicleTypeCheck(vehiecleTypeValue = {"1","2"})
    private String vehicleType;


}
