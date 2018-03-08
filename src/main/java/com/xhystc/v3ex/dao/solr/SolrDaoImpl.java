package com.xhystc.v3ex.dao.solr;

import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.SolrHighLightInform;
import com.xhystc.v3ex.model.vo.SolrSearchResult;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.StringUtils;

import java.io.IOException;
import java.util.*;

public class SolrDaoImpl implements SolrDao
{
	private HttpSolrClient client;

	private static final Logger logger = Logger.getLogger(SolrDaoImpl.class);

	@Override
	public SolrSearchResult queryQuestion(String queryString, String tag, int start, int rows)
	{
		SolrSearchResult ret = new SolrSearchResult();
		List<SolrHighLightInform> res = new ArrayList<>(rows);
		QueryResponse queryResponse;
		SolrQuery query = new SolrQuery(queryString);
		if(!StringUtils.isEmpty(tag)){
			query.add("fq",tag);
		}
		query.setStart(start);
		query.setRows(rows);
		try
		{
			queryResponse = client.query(query);
		} catch (SolrServerException | IOException e)
		{
			logger.info(e.getMessage());
			ret.setTotal(0);
			return ret;
		}
		SolrDocumentList documents = queryResponse.getResults();
		if(documents==null){
			ret.setTotal(0);
			return ret;
		}

		Map<String,Map<String,List<String>>> highlight = queryResponse.getHighlighting();
		ret.setTotal(documents.getNumFound());
		for (SolrDocument document : documents){
			SolrHighLightInform inform = new SolrHighLightInform();
			String id = document.getFieldValue("id").toString();
			Map<String,List<String>> hl = highlight.get(id);
			inform.setId(Long.decode(id));
			inform.setContentHighlight(hl.get("content")!=null?hl.get("content").get(0):null);
			inform.setTitleHighlight(hl.get("title")!=null?hl.get("title").get(0):null);

			res.add(inform);
		}
		ret.setHighLightInforms(res);
		return ret;
	}


	@Override
	public boolean addQuestion(Question question, double score)
	{
		SolrInputDocument inputDocument = new SolrInputDocument();
		inputDocument.setField("id",question.getId());
		inputDocument.setField("content",question.getContent());
		inputDocument.setField("title",question.getTitle());
		inputDocument.setField("score",score);
		inputDocument.setField("tag",question.getTag().getName());

		try
		{
			client.add(inputDocument);
		} catch (SolrServerException | IOException e)
		{
			logger.info(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteQuestion(Long id)
	{
		try
		{
			client.deleteById(String.valueOf(id));
		} catch (SolrServerException | IOException e)
		{
			logger.info(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean updateQuestion(Long id,String field,String value)
	{
		SolrInputDocument inputDocument = new SolrInputDocument();
		inputDocument.setField("id",id);
		Map<String,String> updateParam = new HashMap<>(1);
		updateParam.put("set",value);
		inputDocument.setField(field,updateParam);

		try
		{
			client.add(inputDocument);
		} catch (SolrServerException | IOException e)
		{
			logger.info(e.getMessage());
			return false;
		}
		return true;
	}


	public HttpSolrClient getClient()
	{
		return client;
	}

	public void setClient(HttpSolrClient client)
	{
		this.client = client;
	}

}
