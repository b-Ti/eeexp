package com.example.cscmp.utils.CSCMP;

/**
 * @author cscmp
 */
public class MetaPath {
	
	public int[] vertex;
	public int[] edge;
	public int pathLen;
	
	public MetaPath(int[] vertex, int[] edge) {
		this.vertex = vertex;
		this.edge = edge;
		//the number of relations in a meta-path
		this.pathLen = edge.length;

		
		if(vertex.length != edge.length + 1) {
			System.out.println("the meta-path is incorrect");
		}
	}
	
	public MetaPath(String metaPathStr) {
		String[] s = metaPathStr.trim().split(" ");
		this.pathLen = s.length / 2;
		this.vertex = new int[pathLen +1];
		this.edge = new int[pathLen];
		
		for(int i = 0;i < s.length;i ++) {
			int value = Integer.parseInt(s[i]);
			if(i % 2 == 0) {
				vertex[i / 2] = value;
			}else {
				edge[i / 2] = value;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0;i < pathLen;i ++) {
			str.append(vertex[i]).append("-").append(edge[i]).append("-");
		}
		str.append(vertex[pathLen]);
		return str.toString();
	}
}
