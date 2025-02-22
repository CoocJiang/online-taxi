package org.example.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.dto.DriverUser;
@Mapper
public interface DirverUserMapper extends BaseMapper<DriverUser>{
    public int selectDriverUserbyCityCode(@Param("cityCode") String cityCode);
}
