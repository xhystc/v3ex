package com.xhystc.v3ex.test;

import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.model.vo.form.QuestionCommentForm;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.youbenzi.mdtool.tool.MDTool;

public class EscapeHTMLTest
{
	public static void main(String[] args){
		QuestionCommentForm form = new QuestionCommentForm();
		form.setContent("## 哈哈\n### 嘻嘻");
		FormUtils.escapeFormModle(form);
		System.out.println(form.getContent());
	}
}



