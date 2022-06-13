package com.example.cscmp.utils.CSCMP;

import com.example.cscmp.entity.BaseBean;
import com.example.cscmp.entity.Categories;

import java.util.*;

/**
 * @author cscmp
 */
public class CSCMPMain {
	
	public static Set<Integer> RunCSCMP(BaseBean que) {
		// 从文件中读取数据
		String path_fix = "S-DBLP/";
		String graph_path = path_fix.concat("graph.txt");
		String vertex_path = path_fix.concat("vertex.txt");
		String edge_path = path_fix.concat("edge.txt");
		DataReader dataReader = new DataReader(graph_path, vertex_path, edge_path);
		int[][] graph = dataReader.readGraph();
		int[] vertexType = dataReader.readVertexType();
		int[] edgeType = dataReader.readEdgeType();
		Config.dataName = "S-DBLP";
		
		// 给定社区搜索的条件
		// 查询节点（27515表示论文High Performance Index Build Algorithms for Intranet Search Engines.）
		// 元路径PTPV
//		MetaPath queryMPath = new MetaPath("0 2 3 5 0 1 2");
		MetaPath queryMPath = new MetaPath(que.getQueryMPath());
		// 查询参数
//		int queryK = 10
		Set<Integer> Sc = new HashSet<>();
		// 约束节点集（31099表示主题engines）
		String[] strings = que.getSc().split(",");
		for (String item: strings) {
			Sc.add(Integer.parseInt(item));
		}
		if (Sc.size()==0){
			return new HashSet<>();
		}
		// 非对称元路径约束+受限元路径约束的社区搜索
		CSCMP CSCMP = new CSCMP(graph, vertexType, edgeType);

		Set<Integer> community = CSCMP.query(que.getQueryId(), queryMPath, que.getQueryK(), Sc);
		System.out.println(community);

		return community;
	}
	
}
