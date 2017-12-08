package com.xhystc.zhihu.test;

import org.markdownj.MarkdownProcessor;

public class MDToolsTest
{
	public static void main(String[] args){
		MarkdownProcessor processor = new MarkdownProcessor();

		System.out.println(processor.markdown("## 测试\n ### &lt;script&gt;alert('xixi')&lt;/script&gt;"));
		System.out.println();
	}

}
