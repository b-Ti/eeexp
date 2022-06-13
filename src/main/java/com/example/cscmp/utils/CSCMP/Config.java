package com.example.cscmp.utils.CSCMP;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

public class Config {
	//stem file paths
	public static String stemFile = "./stemmer.lowercase.txt";
	public static String stopFile = "./stopword.txt";

	public static String root = "F:/UNSW/HINData"; // 2604实验室电脑
	{
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File com=fsv.getHomeDirectory();
		//automatically obtain the path of Desktop
		root = com.getPath();
	}
	
	//SmallDBLP
	public static String smallDBLPRoot = root + "\\HIN\\dataset\\SmallDBLP\\";
	public static String smallDBLPGraph = smallDBLPRoot + "graph.txt";
	public static String smallDBLPVertex = smallDBLPRoot + "vertex.txt";
	public static String smallDBLPEdge = smallDBLPRoot + "edge.txt";

	//DBLP
	public static String dblpRoot = root + "\\HIN\\dataset\\newDBLP\\";
	public static String dblpGraph = dblpRoot + "graph.txt";
	public static String dblpVertex = dblpRoot + "vertex.txt";
	public static String dblpEdge = dblpRoot + "edge.txt";
	
	//IMDB
	public static String IMDBRoot = root + "\\HIN\\dataset\\IMDB\\";
	public static String IMDBGraph = IMDBRoot + "graph.txt";
	public static String IMDBVertex = IMDBRoot + "vertex.txt";
	public static String IMDBEdge = IMDBRoot + "edge.txt";
	
	//Foursquare
	public static String FsqRoot = root + "\\HIN\\dataset\\FourSquare\\";
	public static String FsqGraph = FsqRoot + "graph.txt";
	public static String FsqVertex = FsqRoot + "vertex.txt";
	public static String FsqEdge = FsqRoot + "edge.txt";
	
	//DBpedia
	public static String dbpediaRoot = root + "\\HIN\\dataset\\DBPedia\\";
	public static String dbpediaGraph = dbpediaRoot + "graph.txt";
	public static String dbpediaVertex = dbpediaRoot + "vertex.txt";
	public static String dbpediaEdge = dbpediaRoot + "edge.txt";
	
	public static int k = 6;
	public static int[][] DBLPType = {{-1,0,1,2},{3,-1,-1,-1},{4,-1,-1,-1},{5,-1,-1,-1}};
	public static int[][] IMDBType = {{-1,0,2,4},{1,-1,-1,-1},{3,-1,-1,-1},{5,-1,-1,-1}};
	public static int[][] FSType = {{-1,0,2,4,6},{1,-1,-1,-1,-1},{3,-1,-1,-1,-1},{5,-1,-1,-1,-1},{7,-1,-1,-1,-1}};
	public static int[][] FBType = {{-1,0,2,4,6},{1,-1,-1,-1,-1},{3,-1,-1,-1,-1},{5,-1,-1,-1,-1},{7,-1,-1,-1,-1}};
	
	public static String dataName;
}
