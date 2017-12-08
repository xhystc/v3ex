package com.xhystc.zhihu.test;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.vo.form.QuestionForm;

public class EscapeHTMLTest
{
	public static void main(String[] args){
		QuestionForm form = new QuestionForm();
		form.setTitle("<h1>haha</h1>");

		FormUtils.escapeHTML(form);
		System.out.println(form.getTitle());
	}
}



