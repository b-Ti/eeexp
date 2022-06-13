package com.example.cscmp.mapper;

import com.example.cscmp.entity.VertexMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface VertexMapMapper {

    public List<VertexMap> select(List<String> ids);
}
