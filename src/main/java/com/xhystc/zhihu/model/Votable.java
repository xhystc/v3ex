package com.xhystc.zhihu.model;

public interface Votable
{
	String type();
	Long id();
	int votes();
	void votes(int vote);
	boolean isVoted();
	void isVoted(boolean is);
}
