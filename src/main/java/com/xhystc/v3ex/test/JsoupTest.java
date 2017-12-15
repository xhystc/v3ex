package com.xhystc.v3ex.test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.web.util.HtmlUtils;

public class JsoupTest
{
	static public void main(String[] args){
		String html = "dfdf<div>lala</div><div class='c1'>xixi<script>alert('xixi');</script></div>";
		Document doc = org.jsoup.Jsoup.parse(html);

		Elements elements = doc.getElementsByAttribute("class");
		for(Element ele : elements){
			ele.removeAttr("class");
		}
		elements = doc.getElementsByAttribute("style");
		for(Element ele : elements){
			ele.removeAttr("style");
		}
		elements = doc.getElementsByTag("script");
		for(Element ele : elements){
			ele.replaceWith(new TextNode(ele.toString()));
		}

		System.out.println(doc.getElementsByTag("body").html());
	}
}
