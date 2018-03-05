package com.xhystc.v3ex.test.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SolrJTest
{
	public static void main(String[] args) throws IOException, SolrServerException, InterruptedException
	{
		HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/v3ex").build();

		SolrQuery query = new SolrQuery("solr");

		QueryResponse queryResponse = client.query(query);
		
		SolrDocumentList documents = queryResponse.getResults();
		Map<String, Map<String, List<String>>> hl = queryResponse.getHighlighting();
		for(Map.Entry<String, Map<String, List<String>>> en : hl.entrySet()){
			System.out.println("hl:"+en.getKey());
			for(Map.Entry<String, List<String>> en2 : en.getValue().entrySet()){
				System.out.print(en2.getKey()+":");
				for(String s : en2.getValue()){
					System.out.print(s+" ");
				}
			}
			System.out.println();
		}
		for(SolrDocument document : documents){
			for(String field : document.getFieldNames()){
				System.out.print(field+":"+document.getFieldValue(field)+" ");
			}

			System.out.println();
		}


	/*	SolrInputDocument inputDocument = new SolrInputDocument();
		inputDocument.setField("id","1");
		Map<String,String> updateParam = new HashMap<>(1);
		updateParam.put("set","0.63");
		inputDocument.setField("score",updateParam);

		UpdateResponse updateResponse = client.add(inputDocument);*/

	/*	Thread.sleep(1000);
		queryResponse = client.query(query);
		documents = queryResponse.getResults();
		for(SolrDocument document : documents){
			for(String field : document.getFieldNames()){
				System.out.print(field+":"+document.getFieldValue(field)+" ");
			}
			System.out.println();
		}


		Thread.sleep(10000);

		queryResponse = client.query(query);
		documents = queryResponse.getResults();
		for(SolrDocument document : documents){

			for(String field : document.getFieldNames()){
				System.out.print(field+":"+document.getFieldValue(field)+" ");
			}
			System.out.println();
		}

		client.commit();
		queryResponse = client.query(query);
		documents = queryResponse.getResults();
		for(SolrDocument document : documents){
			for(String field : document.getFieldNames()){
				System.out.print(field+":"+document.getFieldValue(field)+" ");
			}
			System.out.println();
		}*/
	}

}



