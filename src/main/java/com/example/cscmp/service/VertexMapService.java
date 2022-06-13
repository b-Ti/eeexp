package com.example.cscmp.service;

import com.example.cscmp.entity.VertexMap;
import com.example.cscmp.mapper.VertexMapMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class VertexMapService {

    @Resource
    public VertexMapMapper vertexMapMapper;

    public List<VertexMap> selectList(List<String> ids){
        return    vertexMapMapper.select(ids);
    }

}
