package com.ex.group.approval.easy.domain;

import com.skt.pe.common.ds.Tree;

@SuppressWarnings("serial")
public class VocCodeTree extends Tree<VocCode> {
	public String[] getNames(String level) {
		String[] result = null;
		int rootSize = 0;
		Tree<VocCode> currentTree = (Tree<VocCode>) getTree(level);
		
		if (currentTree != null && currentTree.getTree() != null) {
			rootSize = currentTree.length();
			result = new String[rootSize];
			for (int i=0; i<rootSize; i++) {
				// 2015-03-05 Join 수정 시작 - 휴가이름 옆에 한도일수까지 나타날 수 있게 요청이 들어와 수정
//				result[i] = currentTree.getTree(i).getItem().getCodeNm();
				result[i] = currentTree.getTree(i).getItem().getCodeNm() + "(" + currentTree.getTree(i).getItem().getTerm() + ")";
				// 2015-03-05 Join 수정 끝
			}
		}
		
		return result;
	}
	
	public static String makeIndex(int level1) {
		return "1_" + level1;
	}
	
	public static String makeIndex(int level1, int level2) {
		return "1_" + level1 + "_" + level2;
	}
	
	public static String makeIndex(int level1, int level2, int level3) {
		return "1_" + level1 + "_" + level2 + "_" + level3;
	}
}
