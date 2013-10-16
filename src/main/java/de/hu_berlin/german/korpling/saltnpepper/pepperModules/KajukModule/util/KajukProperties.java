package de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util;

import java.util.Collection;
import java.util.Vector;

import de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util.xpath.XPathExpression;



public class KajukProperties 
{	
	
	private static final String SPAN_ANNO=
			//"line," +
			"doc," +
			//"pb," +
			"newline,"+
			"newpage,"+
			"lb";
	
	private static final String TOK_ANNO = 
			"V, " +
			"lb, "+
			"VV, " +
			"HV," +
			"AcI," +
			"AD," +
			"ADJGr, " +
			"ADV, " +
			"akk, " +
			"AP, " +
			"FOK, " +
			"FV, " +
			"HMV, " +
			"J, " +
			"KON, " +
			"KOR, " +
			"KV, " +
			"LASSEN, " +
			"MV, " +
			"obj, " +
			"obl, " +
			"prae, " +
			"PGr, " +
			"Phras, " +
			"praed, " +
			"RF, " +
			"SUB, " +
			"IP, " +
			"subj, " +
			"TUN, " +
			"VFin, " +
			"VP, " +
			"PV, " +
			"VV, " +
			"XX, " +
			"praed," +
			"subj," +
			"J," +
			"FOK," +
			"Inf," +
			"VOR," +
			"";
	
	private static final String ELLIPSIS = 
			"";
	
	public KajukProperties()
	{
		
	}
	
	private Collection<XPathExpression>spanList = null;
	public Collection<XPathExpression> h_getSpanList()
	{
		if(spanList == null)
		{
			Collection<XPathExpression> xPathList = extractXPathExpression(SPAN_ANNO);
			if(spanList == null)
				spanList = xPathList;
		}
		return spanList;
	}
	
	private Collection<XPathExpression> tokAnnoList = null;
	public Collection<XPathExpression> h_getTokAnnoList()
	{
		if(tokAnnoList == null)
		{
			Collection<XPathExpression> xPathList = extractXPathExpression(TOK_ANNO);
			if(tokAnnoList == null)
				tokAnnoList = xPathList;
		}
		return tokAnnoList;
	}
	
	private Collection<XPathExpression> ellipsisList = null;
	public Collection<XPathExpression> h_getEllispsisList()
	{
		if(ellipsisList == null)
		{
			Collection<XPathExpression> xPathList = extractXPathExpression(ELLIPSIS);
			if(ellipsisList == null)
				ellipsisList = xPathList;
		}
		return ellipsisList;
	}
	
	private synchronized Collection<XPathExpression> extractXPathExpression(String propName)
	{
		Collection<XPathExpression> xPathList = new Vector<XPathExpression>();

		String[] xPathListStr = propName.split(",");
		if(xPathList!=null)
		{
			for(String xPathString : xPathListStr)
				xPathList.add(new XPathExpression(xPathString.trim()));
		}
		return xPathList;
		
	}
}
