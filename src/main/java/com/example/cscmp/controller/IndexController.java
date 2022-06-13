package com.example.cscmp.controller;


import com.example.cscmp.entity.*;
import com.example.cscmp.framework.ResultBean;
import com.example.cscmp.service.VertexMapService;
import com.example.cscmp.utils.CSCMP.CSCMPMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author cscmp
 */

@RestController
@RequestMapping("/api/cscmp")
public class IndexController {
    @Autowired
    private VertexMapService vertexMapService;
    @GetMapping("/getData")
    public ResultBean<Set<Integer>> cscmpQuery(BaseBean baseBean){
        Map<String, List<Object>> communityView = new HashMap<>();
        List<Object> categoriesList = new ArrayList<>();
        List<Object> communityList = new ArrayList<>();
        List<Object> linksList = new ArrayList<>();
        communityView.put("categories", categoriesList);
        communityView.put("nodes", communityList);
        communityView.put("links",linksList);

        Set<Integer> resID = CSCMPMain.RunCSCMP(baseBean);
        System.out.println(resID.toString());
        resID.add(baseBean.getQueryId());
        List<VertexMap> vertexMaps = vertexMapService.selectList(resID.stream().map(item->Integer.toString(item)).collect(Collectors.toList()));
        Nodes nodes;
        Links links;
        for (VertexMap item : vertexMaps){
            nodes = new Nodes();
            nodes.setId(item.getF1());
            nodes.setName(item.getF4());
            nodes.setCategory(item.getF2());
            nodes.setValue(Integer.parseInt(item.getF1()));
            nodes.setQueryId(Integer.parseInt(item.getF1()));
            nodes.setQueryK(baseBean.getQueryK());
            nodes.setQueryMPath(baseBean.getQueryMPath());
            communityList.add(nodes);
            if (!item.getF1().equals(Integer.toString(baseBean.getQueryId()))){
                links = new Links();
                links.setSource(Integer.toString(baseBean.getQueryId()));
                links.setTarget(item.getF1());
                linksList.add(links);
            }

        }
        List<String> vertexTypes = vertexMaps.stream().map(item->item.getF2()).distinct().collect(Collectors.toList());
        Categories category;
        for (String item:vertexTypes){
            category = new Categories();
            category.setName(item);
            categoriesList.add(category);
        }
        return new ResultBean(0, "查询成功", resID.size(), communityView);
    }

}
