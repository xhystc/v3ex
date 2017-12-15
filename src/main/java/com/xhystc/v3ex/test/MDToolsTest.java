package com.xhystc.v3ex.test;

import com.youbenzi.mdtool.tool.MDTool;
import org.markdownj.MarkdownProcessor;

public class MDToolsTest
{
	public static void main(String[] args){
		MarkdownProcessor processor = new MarkdownProcessor();
		//## 测试
// ### &lt;script&gt;alert('xixi')&lt;/script&gt;
		System.out.println(MDTool.markdown2Html("hha+sds"));
	}

}
