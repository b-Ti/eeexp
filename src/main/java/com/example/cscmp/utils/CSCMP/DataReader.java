package com.example.cscmp.utils.CSCMP;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class DataReader {
	// 文件名
	private String graphFile = null;
	private String vertexFile = null;
	private String edgeFile = null;
	
	// 记录节点数
	private int vertexNum = 0;
	// 记录边数
	private int edgeNum = 0;
	
	public DataReader(String graphFile, String vertexFile, String edgeFile) {
		this.graphFile = graphFile;
		this.vertexFile = vertexFile;
		this.edgeFile = edgeFile;
		
		//compute the number of nodes
		try{
			ClassPathResource resource = new ClassPathResource(graphFile);
			String tempPath =System.getProperty("java.io.tmpdir") + System.currentTimeMillis()+".txt";
			File test = new File(tempPath);
			FileUtils.copyToFile(resource.getInputStream(),test);
			long fileLength = test.length(); 
			LineNumberReader rf = new LineNumberReader(new FileReader(test));
			if (rf != null) {
				rf.skip(fileLength);
				//obtain the number of nodes
				vertexNum = rf.getLineNumber();
			}
			rf.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//return the graph edge information
	public int[][] readGraph() {
		int graph[][] = new int[vertexNum][];
		try{
			ClassPathResource resource = new ClassPathResource(graphFile);
			String tempPath =System.getProperty("java.io.tmpdir") + System.currentTimeMillis()+".txt";
			File test = new File(tempPath);
			FileUtils.copyToFile(resource.getInputStream(),test);
			BufferedReader stdin = new BufferedReader(new FileReader(test));
						
			String line = null;
			while((line = stdin.readLine()) != null){
				String s[] = line.split(" ");
				int vertexId = Integer.parseInt(s[0]);
				
				int nb[] = new int[s.length - 1];
				for(int i = 1;i < s.length;i ++) {
					nb[i - 1] = Integer.parseInt(s[i]);
				}
				graph[vertexId] = nb;
				
				edgeNum += nb.length / 2;
			}
			stdin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println(graphFile + " |V|=" + vertexNum + " |E|=" + edgeNum / 2);
		//each edge is bidirectional
		
		return graph;
	}
	
	//return the type of each vertex
	public int[] readVertexType() {
		int vertexType[] = new int[vertexNum];
		
		try{
			ClassPathResource resource = new ClassPathResource(vertexFile);
			String tempPath =System.getProperty("java.io.tmpdir") + System.currentTimeMillis()+".txt";
			File test = new File(tempPath);
			FileUtils.copyToFile(resource.getInputStream(),test);
			BufferedReader stdin = new BufferedReader(new FileReader(test));
			String line = null;
			while((line = stdin.readLine()) != null){
				String s[] = line.split(" ");
				int id = Integer.parseInt(s[0]);
				int type = Integer.parseInt(s[1]);
				vertexType[id] = type;
			}
			stdin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return vertexType;
	}
	
	//return the type of each edge
	public int[] readEdgeType() {
		int edgeType[] = new int[edgeNum];
		try{
			ClassPathResource resource = new ClassPathResource(edgeFile);
			String tempPath =System.getProperty("java.io.tmpdir") + System.currentTimeMillis()+".txt";
			File test = new File(tempPath);
			FileUtils.copyToFile(resource.getInputStream(),test);
			BufferedReader stdin = new BufferedReader(new FileReader(test));
			String line = null;
			while((line = stdin.readLine()) != null){
				String s[] = line.split(" ");
				int id = Integer.parseInt(s[0]);
				int type = Integer.parseInt(s[1]);
				edgeType[id] = type;
			}
			stdin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return edgeType;
	}
}
