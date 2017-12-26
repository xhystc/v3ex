package com.xhystc.v3ex.test;

import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.vo.form.QuestionCommentForm;

public class EscapeHTMLTest
{
	public static void main(String[] args){
		QuestionCommentForm form = new QuestionCommentForm();
		form.setContent("## 哈哈\n### 嘻嘻");
		CommonUtils.escapeFormModle(form);
		System.out.println(form.getContent());
	}
}



