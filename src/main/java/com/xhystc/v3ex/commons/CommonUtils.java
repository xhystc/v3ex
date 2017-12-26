package com.xhystc.v3ex.commons;

import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.json.Problem;
import com.xhystc.v3ex.service.UserService;
import com.youbenzi.mdtool.tool.MDTool;
import org.apache.shiro.SecurityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.markdownj.MarkdownProcessor;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.util.HtmlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CommonUtils
{

	public static final String LINK_DIVIDE = ",";
	public static final String LINK_BEGIN = "#!(";
	public static final char LINK_END = ')';
	public static final String LINK_TEMPLATE = "#!(%s,%s)";

	public static final char AT_BEGIN = '@';

	public static User getCurrentUser(){
		return (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
	}

	public static void setCurrentUser(User user){
		SecurityUtils.getSubject().getSession().setAttribute("currentUser",user);
	}

	public static void escapeFormModle(Object o){
		Class clazz = o.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			if(field.getType()!=String.class)
			{
				continue;
			}
			try
			{
				String fieldName = field.getName();
				char c = (char) (fieldName.charAt(0)+('A'-'a'));
				Method setMethod = clazz.getMethod("set"+c+fieldName.substring(1),String.class);
				Method getMethod = clazz.getMethod("get"+c+fieldName.substring(1));
				if(setMethod==null || getMethod==null){
					continue;
				}
				String s = (String)getMethod.invoke(o);

				Markdown markdown = field.getAnnotation(Markdown.class);

				if(s!=null){
					s = s.trim();
					if(markdown ==null ){
						setMethod.invoke(o,HtmlUtils.htmlEscape(s));
					}else {
						s = s.replace("\n","<br/>");
						s = MDTool.markdown2Html(s);
						Document doc = org.jsoup.Jsoup.parse(s);
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
						MarkdownProcessor processor = new MarkdownProcessor();
						setMethod.invoke(o,doc.getElementsByTag("body").html());

					}
					setMethod.invoke(o,s.replace("#!(","#！("));
				}

			}
			catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored)
			{

			}

		}
	}

	public static List<Integer> pageButtons(int currPage,int lastPage){
		List<Integer> ret  = new ArrayList<>();
		if (lastPage<=0){
			return ret;
		}
		if(currPage - 1 < 4){
			for(int i=1;i<=currPage;i++){
				ret.add(i);
			}
			for(int i=currPage+1;i<=lastPage && i<=currPage+2;i++){
				ret.add(i);
			}
			if(ret.get(ret.size()-1)!=lastPage){
				ret.add(0);
				ret.add(lastPage);
			}
		}else if(lastPage-currPage<4){
			ret.add(1);
			ret.add(0);
			for(int i=currPage-2;i<=lastPage;i++){
				ret.add(i);
			}
		}else {
			ret.add(1);
			ret.add(0);
			for(int i = currPage-2;i<=currPage+2;i++){
				ret.add(i);
			}
			ret.add(0);
			ret.add(lastPage);
		}
		return ret;
	}

	public static String wrapLinkHtml(String url,String content){
		String t = "<a href= %s>%s</a>";
		return String.format(t,url,content);
	}

	public static String AtEscape(String content, UserService userService){
		StringBuilder sb = new StringBuilder();
		int ptr = 0;
		while (ptr < content.length()){
			int index = content.indexOf(AT_BEGIN,ptr);

			if(index<0){
				sb.append(content.substring(ptr));
				break;
			}
			sb.append(content.substring(ptr,index));
			int nextIndex = nextIndex(content,index+1);
			String username = content.substring(index+1,nextIndex);
			User user  = userService.getUserByName(username);
			if(user!=null){
				String s = String.format(LINK_TEMPLATE,"/user/"+user.getId(),"@"+user.getName());
				sb.append(s);
				ptr = nextIndex;
			}else {
				sb.append(content.substring(index,nextIndex));
				ptr = nextIndex;
			}
		}
		return sb.toString();
	}


	public static int nextIndex(String content,int pos,char end){
		for(int i = pos;i<content.length();i++){
			if(content.charAt(i) == end){
				return i;
			}
		}
		return content.length();
	}

	public static int nextIndex(String content,int pos){
		for(int i = pos;i<content.length();i++){
			if(Character.isWhitespace(content.charAt(i))){
				return i;
			}
		}
		return content.length();
	}

	public static boolean handleErrors(Model model,Errors errors){
		if(errors.hasErrors()){
			Set<Problem> problems = CommonUtils.getProblems(errors);
			model.addAttribute("problems",problems);
			return true;
		}
		return false;
	}

	private static Set<Problem> getProblems(Errors errors){
		Set<Problem> problems = new HashSet<>(errors.getErrorCount());
		for(FieldError error : errors.getFieldErrors()){
			Problem problem = new Problem(error.getField(),null,error.getDefaultMessage());
			problems.add(problem);
		}
		return problems;
	}
}






