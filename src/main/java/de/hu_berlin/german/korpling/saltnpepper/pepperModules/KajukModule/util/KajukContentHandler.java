/**
 * Copyright 2009 Humboldt University of Berlin, INRIA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util;



import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.osgi.service.log.LogService;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.neovisionaries.i18n.LanguageCode;

import de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util.xpath.XPathExpression;
import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDocumentGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SPointingRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SSpan;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SSpanningRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.STextualDS;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SToken;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.tokenizer.Tokenizer;
//import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.tokenizer.Tokenizer;
//import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.tokenizer.*;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAbstractAnnotation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAnnotation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SLayer;
/**
 * 
 * 
 * @author ze0
 *
 */
public class KajukContentHandler extends DefaultHandler2 implements ContentHandler 
{

	private static final String SPAN_ANNO=
			"doc,newline,newpage,lb";
	
	private static final String TOK_ANNO = 
			"V,lb,VV,HV,AcI,AD,ADJGr,ADV,akk,AP,FOK,FV,HMV,J,KON,KOR,KV,LASSEN,MV," +
			"obj,obl,prae,PGr,Phras,praed,RF,SUB,IP,subj,TUN,VFin,VP,PV,VV,XX,praed,subj,J,FOK,Inf,VOR,";
	
	private static final String ELLIPSIS = 
			"";
	/** **/
	private SDocument sDocument;
	private STextualDS currentSDS;
	private XPathExpression currentXPath = null;
	private SDocumentGraph sDocumentGraph = null; 
	private LinkedList<String> openToken;
	private HashMap<String, LinkedList<SToken>> openSpans;
	private int multiLevelLb;
	private String lastLine;
	private String lastPage;
	private HashMap<String, LinkedList<SSpan>> houseNumbers;
	private boolean isEllipsis;
	private String currentEllipsis;
	private StringBuilder stringBuilder;
	private Tokenizer tokenizer;
	
	private SLayer junktionen;
	private SLayer lexikalische_annotationen;
	private SLayer syntaktische_annotationen;
	private SLayer ellipsenannotationen;
	private SLayer sachverhaltsdarstellungen;
	private SLayer graphische_annotationen;
	private SLayer hilfsannotationen;

	private LogService logService;
	
	// this is the hashmap for the annotations 
	private HashMap<String, EList<SAbstractAnnotation>> annotationMap;
	
	public KajukContentHandler(SDocument sDocument, LogService logService)
	{
		this.logService = logService;
		this.sDocument = sDocument;
		this.currentXPath = new XPathExpression();
		this.currentSDS = SaltFactory.eINSTANCE.createSTextualDS();
		this.openToken = new LinkedList<String>();
		this.openSpans = new HashMap<String,LinkedList<SToken>>();
		sDocumentGraph = sDocument.getSDocumentGraph();
		sDocumentGraph.addSNode(currentSDS);
		this.multiLevelLb = 0;
		this.annotationMap = new HashMap<String, EList<SAbstractAnnotation>>();
		this.lastLine = "";
		this.lastPage = "";
		this.houseNumbers = new HashMap<String, LinkedList<SSpan>>();
		this.isEllipsis = false;
		this.currentEllipsis = "";
		this.stringBuilder = new StringBuilder();
		this.tokenizer = new Tokenizer();
		
		this.junktionen	= SaltFactory.eINSTANCE.createSLayer();
		this.junktionen.setSName("junktionen");
		this.sDocumentGraph.addSLayer(junktionen);
		this.lexikalische_annotationen		= SaltFactory.eINSTANCE.createSLayer();
		this.lexikalische_annotationen.setSName("lexikalische_annotationen");
		this.sDocumentGraph.addSLayer(lexikalische_annotationen);
		this.syntaktische_annotationen 		= SaltFactory.eINSTANCE.createSLayer();
		this.syntaktische_annotationen.setSName("syntaktische_annotationen");
		this.sDocumentGraph.addSLayer(syntaktische_annotationen);
		this.ellipsenannotationen			= SaltFactory.eINSTANCE.createSLayer();
		this.ellipsenannotationen.setSName("ellipsenannotationen");
		this.sDocumentGraph.addSLayer(ellipsenannotationen);
		this.sachverhaltsdarstellungen		= SaltFactory.eINSTANCE.createSLayer();
		this.sachverhaltsdarstellungen.setSName("sachverhaltsdarstellungen");
		this.sDocumentGraph.addSLayer(sachverhaltsdarstellungen);
		this.graphische_annotationen		= SaltFactory.eINSTANCE.createSLayer();
		this.graphische_annotationen.setSName("graphische_annotationen");
		this.sDocumentGraph.addSLayer(graphische_annotationen);
		this.hilfsannotationen				= SaltFactory.eINSTANCE.createSLayer();
		this.hilfsannotationen.setSName("hilfsannotationen");
		this.sDocumentGraph.addSLayer(hilfsannotationen);
	}
	
	
	/**
	 * At first we get the text so far by currentSDS.getSText(), which will be appended to a new textbuffer. With this, we are 
	 * able to determine the start and end indexes of the new token. After the creation of the token, a STextualRelation between the token 
	 * and the STextualDS is created with the use of the sStart and sEnd indexes.
	 * 
	 * @param ch	the character sequence to be evaluated
	 * @param start	the index of the sequence relative to the STextualDS
	 * @param length	the length of the sequence
	 */
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		StringBuffer textBuf= new StringBuffer();
		for (int i= start; i< start+length; i++)
			textBuf.append(ch[i]);
		
		String text= textBuf.toString();
		
		if(	text!=null && 
			text.length()>0)
		{
			textBuf = new StringBuffer();
			String containedText = currentSDS.getSText();
			if(containedText != null)	// if not empty, the STextualDS is the first part of the contained text
				textBuf.append(removeControlCharacters(containedText));
			int sStart = textBuf.toString().trim().length();
			textBuf.append(removeControlCharacters(text)+" ");
			currentSDS.setSText(textBuf.toString());
			int sEnd = textBuf.toString().trim().length();
			
			String check = removeControlCharacters(currentSDS.getSText().substring(sStart, sEnd));
			
			if(!check.equals(""))
			{
				if(this.openToken != null && this.openToken.peek() != null)
				{
					EList<SToken> tokenized = null;
					STextualDS retSTextualDS = SaltFactory.eINSTANCE.createSTextualDS();;
					retSTextualDS.setSText(check);
					this.tokenizer.setsDocumentGraph(this.sDocumentGraph);
					tokenized = this.tokenizer.tokenize(this.currentSDS, LanguageCode.de, sStart, sEnd);
					for(SToken sToken : tokenized)
					{
						h_copySAbstractAnnotations(sToken);
						
						for(String str : this.openSpans.keySet())
		                {
		                	LinkedList<SToken> sTokenList = this.openSpans.get(str);
		                	if(sTokenList != null)
		                		sTokenList.add(sToken);
		                	else
		                	{
		                		sTokenList = new LinkedList<SToken>();
		                		sTokenList.add(sToken);
		                	}
		                	
		                	this.openSpans.put(str, sTokenList);
		                }
					}
				}
			}
		}
	}
	
	/**
	 * Evaluates the elements of the xml file according to the corpus structure. The main focus lies on putting 
	 * the opened tags onto a stack for later use, depending on the tagname itself. 
	 * <p>
	 * There are two special tags that need to be handeled differently, namely <b>newline</b> and <b>newpage</b>. 
	 * Additionally this method is responsible to detect multi-level spans of the <b>lb</b>-tag and ellipsis.
	 */
	public void startElement(	String uri, 
								String localName, 
								String qName,
								Attributes attributes) 
			throws SAXException 
	{
		// we ignore those, because we replaced them with newline and newpage
		if(qName.equals("pb") || qName.equals("line"))
			return;
		
		this.currentXPath.addStep(qName);
		
		if(this.annotationMap.get(qName) != null)
		{
			if(qName.equals("lb"))
			{
				this.multiLevelLb++;
				this.annotationMap.put("lb_"+this.multiLevelLb, h_createSAbstractAnnotations(qName, attributes));
			}
			else
				this.logService.log(LogService.LOG_WARNING, qName + " already in AnnotationMap, should not be there.. ");
		}
		else
		{
			this.annotationMap.put(qName, h_createSAbstractAnnotations(qName, attributes));
		}
		
		for(int i = 0; i<=attributes.getLength(); i++)
		{
			if(attributes.getQName(i) != null && (attributes.getQName(i).equals("type") || attributes.getQName(i).equals("ADDtype")) && attributes.getValue(i).equals("E"))
			{
				this.isEllipsis = true;
				this.currentEllipsis = qName;
			}
		}


		if(h_matches(h_getTokAnnoList(), this.currentXPath))
		{
			this.openToken.add(qName);
		}
		
		if(h_matches(h_getSpanList(), this.currentXPath) )
		{
			if(qName.equals("newline"))
			{
				
				if(this.openSpans.get(qName) != null)
				{
					SSpan sSpan = SaltFactory.eINSTANCE.createSSpan();
					sSpan.setSName("line");
					SAnnotation sAnno = SaltFactory.eINSTANCE.createSAnnotation();
					sAnno.setName("line");
					sAnno.setValue(this.lastLine);
					if(sAnno.getName() != null) // this test is to make sure there are no "null" spans
					{
						sAnno.setSName("line");
						sSpan.addSAnnotation(sAnno);
						this.sDocument.getSDocumentGraph().addSNode(sSpan);
						//create spanning relations to all tokens
						for(SToken sToks : this.openSpans.get(qName)) 
						{
							SSpanningRelation sSpanRel= SaltFactory.eINSTANCE.createSSpanningRelation();
							sSpanRel.setSSpan(sSpan);
							sSpanRel.setSToken(sToks);
							sDocument.getSDocumentGraph().addSRelation(sSpanRel);
						}
					}
					this.openSpans.remove(qName);
				}
				else // this is the case if it is the first newline element evah
				{
					this.openSpans.put(qName, new LinkedList<SToken>());
					this.logService.log(LogService.LOG_DEBUG, "put "+qName+" to openSpans");
				}
				for(int i = 0; i<=attributes.getLength(); i++)
				{
					if(attributes.getQName(i) != null && attributes.getQName(i).equals("n"))
					{
						this.lastLine = attributes.getValue(i);
					}
				}
			}
			if(qName.equals("newpage"))
			{
				
				if(this.openSpans.get(qName) != null)
				{
					SSpan sSpan = SaltFactory.eINSTANCE.createSSpan();
					sSpan.setSName("page");
					SAnnotation sAnno = SaltFactory.eINSTANCE.createSAnnotation();
					sAnno.setName("page");
					sAnno.setValue(this.lastPage);
					if(sAnno.getName() != null) // this test is to make sure there are no "null" spans
					{
						sAnno.setSName("page");
						sSpan.addSAnnotation(sAnno);
						this.sDocument.getSDocumentGraph().addSNode(sSpan);
						//create spanning relations to all tokens
						for(SToken sToks : this.openSpans.get(qName)) 
						{
							SSpanningRelation sSpanRel= SaltFactory.eINSTANCE.createSSpanningRelation();
							sSpanRel.setSSpan(sSpan);
							sSpanRel.setSToken(sToks);
							sDocument.getSDocumentGraph().addSRelation(sSpanRel);
						}
					}
					this.openSpans.remove(qName);
				}
				else // this is the case if it is the first newline element evah
				{
					this.openSpans.put(qName, new LinkedList<SToken>());
				}
				for(int i = 0; i<=attributes.getLength(); i++)
				{
					if(attributes.getQName(i) != null && attributes.getQName(i).equals("n"))
					{
						this.lastPage = attributes.getValue(i);
					}
				}
				
			}
			
			this.openSpans.put(qName, new LinkedList<SToken>());
		}
		
	}

	/**
	 * Creates the spans if a tag closes according to the specified 
	 */
	public void endElement(	String uri, 
							String localName, 
							String qName)
			throws SAXException 
	{
			if(this.stringBuilder!=null && this.stringBuilder.toString().length() > 0)
			{
				this.stringBuilder.toString();
				this.stringBuilder = null;
			}
			// we ignore those, because we replaced them with newpage and newline
			if(qName.equals("pb") || qName.equals("line"))
				return;
			
			if(h_matches(h_getTokAnnoList(), this.currentXPath))
			{
				this.openToken = h_removeOpenToken(this.openToken,qName);
			}
			
			if(this.currentEllipsis.equals(qName) || qName.equals("doc"))
			{
				this.currentEllipsis = "";
				this.isEllipsis = false;
			}
			
			if(h_matches(h_getSpanList(), this.currentXPath))
			{
				if("newline".equals(qName) || qName.equals("newpage"))
				{
					// remember to start a span with the previous line number
					this.openSpans.put(qName, new LinkedList<SToken>());
				}
				else
				{
					// here we create the spans for the lbs and the doc 
					SSpan sSpan = SaltFactory.eINSTANCE.createSSpan();
					sSpan.setSName(qName);
					SAnnotation sAnno= SaltFactory.eINSTANCE.createSAnnotation();
					sAnno = h_getSAbstractSpanAnnotation(qName, this.annotationMap.get(qName));
					
					// get every housenumber from the attributes of the corresponding startElement()
					LinkedList<String> numbers = new LinkedList<String>();
					numbers = h_getHouseNumbers(qName);
					for(String nr : numbers)
					{
						LinkedList<SSpan> spans = this.houseNumbers.get(nr);
						if(spans != null)
						{
							spans.add(sSpan);
							this.houseNumbers.put(nr, spans);
						}
						else
						{
							spans = new LinkedList<SSpan>();
							spans.add(sSpan);
							this.houseNumbers.put(nr, spans);
						}
					}
					if(sAnno.getName() != null) // this test is to make sure there are no "null" spans
					{
						sSpan.addSAnnotation(sAnno);
						if(qName.equals("lb"))
						{
							sAnno = SaltFactory.eINSTANCE.createSAnnotation();
							sAnno.setSName("lb");
							sAnno.setSValue("lb");
							sSpan.addSAnnotation(sAnno);
						}
						this.sDocument.getSDocumentGraph().addSNode(sSpan);
						for(SToken sToks : this.openSpans.get(qName)) 
						{
							SSpanningRelation sSpanRel= SaltFactory.eINSTANCE.createSSpanningRelation();
							sSpanRel.setSSpan(sSpan);
							sSpanRel.setSToken(sToks);
							sDocument.getSDocumentGraph().addSRelation(sSpanRel);
						}
					}
					this.openSpans.remove(qName);
				}
			}
			this.currentXPath.removeLastStep();
			
			if(this.annotationMap.get(qName) != null)
			{
				if(qName.equals("lb") && this.multiLevelLb>0)
				{
					this.annotationMap.remove("lb_"+this.multiLevelLb);
					this.multiLevelLb--;
				}
				else
					this.annotationMap.remove(qName);
			}
			else
			{
				this.logService.log(LogService.LOG_WARNING, qName + " was already removed or never in the annotationMap");
			}

	}

	
	public String removeControlCharacters(String text)
	{
		if(text != null && text.length() > 0)
		{
			text = text.trim();
			if(text.contains("\n")) text = text.replace("\n","");
			if(text.contains("\r"))text = text.replace("\r","");
			if(text.contains("\t"))text = text.replace("\t","");
			if(text.contains("\f"))text = text.replace("\f","");
			if(text.contains("\b"))text = text.replace("\b","");
			// replace all multi-whitespaces with single whitespaces
			text.replaceAll("\\p{Cntrl}", "");
			text = text.trim().replaceAll(" +", " ");
		}
		return text;
	}
	
	/**
	 * This method creates the appropriate Annotations according to xml_nach_AWP_Martin20120827.xlsx
	 * <p> 
	 *  e.g. the element &lt;AD&gt;&lt;/AD&gt; equals J_type="AD" 
	 *  
	 * @param qName the current element
	 * @param attributes the attributes of the element, may be null 
	 * @return 
	 */
	private EList<SAbstractAnnotation> h_createSAbstractAnnotations(	String qName,
																		Attributes attributes)
	{
		EList<SAbstractAnnotation> annoList = null;
		
		for(int i =0; i<=attributes.getLength(); i++)
		{
			if(annoList == null)
				annoList = new BasicEList<SAbstractAnnotation>();
			if(i!=attributes.getLength())
				currentXPath.addStep("@"+attributes.getQName(i));
			SAbstractAnnotation sAnno = null;
				
			sAnno = SaltFactory.eINSTANCE.createSAnnotation();
			
			if(sAnno!=null)
			{
				if(i == attributes.getLength())
				{
					ConversionClass.convert(sAnno, qName, null, null, this.logService);
				}
				else
					ConversionClass.convert(sAnno, qName, attributes.getQName(i),attributes.getValue(i), this.logService);
				if(sAnno.getSName() == null && !qName.equals("doc") && !qName.equals("newline") && !qName.equals("newpage") && !qName.equals("line") && !qName.equals("pb"))
					this.logService.log(LogService.LOG_WARNING, "sAnno still null - qName = " + qName);
				else
				{
					annoList.add(sAnno);
				}
			}
			if(i!=attributes.getLength())
				currentXPath.removeLastStep();
		}
		return annoList;
	}
	
	/**
	 * Returns the annotation saved in the annotationList under specific conditions. No empty entries and 
	 * no identical key-value
	 * 
	 * @param qName	the name of the current tag
	 * @param annotations	the list of annotations
	 * @return the specified SAnnotation
	 */
	private SAnnotation h_getSAbstractSpanAnnotation(	String qName, EList<SAbstractAnnotation> annotations )
	{
		SAnnotation sAnno = null;
		sAnno = SaltFactory.eINSTANCE.createSAnnotation();
		for(SAbstractAnnotation anno : annotations)
		{
			if(anno != null && anno.getName()!= null && anno.getValue() != null && !anno.getName().equals(anno.getValue()))
			{
				sAnno.setName(anno.getName());
				sAnno.setValue(anno.getValue());
			}
		}
		return sAnno;
}
	
	/**
	 * Returns a List of housenumbers gathered from the attributes as strings.
	 * @param qName	the name of the current tag
	 * @return a list of numbers as strings
	 */
	private LinkedList<String> h_getHouseNumbers(String qName)
	{
		LinkedList<String> list = new LinkedList<String>();
		for(SAbstractAnnotation item : this.annotationMap.get(qName))
		{
			if(item != null && item.getSName() != null && item.getSName().equals("lb_n"))
			{
				for(String str : item.getValueString().split(","))
				{
					if(str.trim() != "")
						list.add(str.trim());
				}
			}
		}
		return list;
	}
	
	/**
	 * This method creates the SPointingRelations according to the saved house numbers saved in the 
	 * <b>houseNumbers</b>-map. The pointing hierarchy is considered as well. 
	 */
	public void endDocument() throws SAXException {
				
		for(String str : this.houseNumbers.keySet())
		{
			for(SSpan span : this.houseNumbers.get(str))
			{
				int index=0;
				for(int i = 0; i < this.houseNumbers.get(str).size(); i++)
				{
					if(this.houseNumbers.get(str).get(i).equals(span))
						index = i+1;
				}
				if(index < this.houseNumbers.get(str).size())//  && (str.equals("1")|| str.equals("1a") || str.equals("3001") || str.equals("3003")))
				{
					
					SPointingRelation sPointingRelation = SaltFactory.eINSTANCE.createSPointingRelation();
					sPointingRelation.setSSource(span);
					sPointingRelation.setSTarget(this.houseNumbers.get(str).get(index));
					this.logService.log(LogService.LOG_DEBUG, "connecting "+span.getIdentifier().getId() + " with " + this.houseNumbers.get(str).get(index).getIdentifier().getValueString()+"\n"+str );
					sDocument.getSDocumentGraph().addSRelation(sPointingRelation);
					String type = "";
					int ret;
					if(str.contains("a") || str.contains("b"))
						ret = Integer.valueOf(str.substring(0, str.length()-1));
					else
						ret = Integer.valueOf(str);
					if(ret >= 1 && ret <= 999)
						type = "SubKon";
					else if(ret >= 1000 && ret <= 1999)
						type = "LatSub";
					else if(ret >= 2000 && ret <= 2999)
						type = "APAD";
					else if(ret >= 3000)
						type = "E";
					if(!type.equals(""))
						sPointingRelation.addSType(type);	
					else
						this.logService.log(LogService.LOG_WARNING, "House number type is wrong!");
				}
			}
		}
		for(String str : this.houseNumbers.keySet())
		{
			if(str.contains("a") || str.contains("b"))
			{
				String get = str.substring(0,str.length()-1);
				SPointingRelation sPointingRelation = SaltFactory.eINSTANCE.createSPointingRelation();
				sPointingRelation.setSSource(this.houseNumbers.get(get).getLast());
				sPointingRelation.setSTarget(this.houseNumbers.get(str).getFirst());
				sDocument.getSDocumentGraph().addSRelation(sPointingRelation);
				String type = "";
				int ret;
				if(str.contains("a") || str.contains("b"))
					ret = Integer.valueOf(str.substring(0, str.length()-1));
				else
					ret = Integer.valueOf(str);
				if(ret >= 1 && ret <= 999)
					type = "SubKon";
				else if(ret >= 1000 && ret <= 1999)
					type = "LatSub";
				else if(ret >= 2000 && ret <= 2999)
					type = "APAD";
				else if(ret >= 3000)
					type = "E";
				if(!type.equals(""))
					sPointingRelation.addSType(type);	
				else
					this.logService.log(LogService.LOG_WARNING, "House number type is wrong!");
			}
		}
		sDocument.setSDocumentGraph(sDocumentGraph);
	}



	private boolean h_matches(Collection<XPathExpression> xprList, XPathExpression xpr)
	{
		if( xprList!= null && xprList.size()>0)
		{
			for(XPathExpression xpr2 : xprList)
			{
				if(XPathExpression.matches(xpr2,xpr))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * This method adds the STokens to their corresponding layers defined 
	 * @param sToken	an SToken
	 */
	private void h_copySAbstractAnnotations(SToken sToken)
	{
		for(int i = 0; i < this.currentXPath.getSteps().size(); i++)
		{
			for(SAbstractAnnotation sAnno : this.annotationMap.get(this.currentXPath.getSteps().get(i)))
			{
				if(sAnno != null && sAnno.getName() != null && h_matches(h_getTokAnnoList(),this.currentXPath) && !this.currentXPath.getSteps().get(i).equals("lb") && !this.currentXPath.getSteps().get(i).equals("lb_n") && !this.currentXPath.getSteps().get(i).equals("doc"))
				{
					SAbstractAnnotation retAnnotation = SaltFactory.eINSTANCE.createSAnnotation();
					retAnnotation.setName(sAnno.getSName());
					retAnnotation.setSValue(sAnno.getSValue());
					
					if(		sAnno.getSName().equals("J") ||
							sAnno.getSName().equals("ADDIR") ||
							sAnno.getSName().equals("IR") ||
							sAnno.getSName().equals("ID")
					)
					{
						this.junktionen.getSNodes().add(sToken);
					}
					
					if(		sAnno.getSName().equals("pos_KAJUK") ||
							sAnno.getSName().equals("pos_ID") ||
							sAnno.getSName().equals("change") 
					)
					{
						this.lexikalische_annotationen.getSNodes().add(sToken);
					}
					
					if(		sAnno.getSName().equals("satzglied") ||
							sAnno.getSName().equals("satzglied_type") ||
							sAnno.getSName().equals("real") ||
							sAnno.getSName().equals("Vorfeld")
					)
					{
						this.syntaktische_annotationen.getSNodes().add(sToken);
					}
					
					if(		sAnno.getSName().contains("_E")
					)
					{
						this.ellipsenannotationen.getSNodes().add(sToken);
					}
					
					if(		sAnno.getSName().contains("lb")
					)
					{
						this.sachverhaltsdarstellungen.getSNodes().add(sToken);
					}
					
					if(		sAnno.getSName().equals("line") ||
							sAnno.getSName().equals("pb") ||
							sAnno.getSName().equals("doc") 
					)
					{
						this.graphische_annotationen.getSNodes().add(sToken);
					}
					
					if(		sAnno.getSName().equals("XX") ||
							sAnno.getSName().equals("viol") ||
							sAnno.getSName().equals("norm") 
					)
					{
						this.hilfsannotationen.getSNodes().add(sToken);
					}
					
					
					
					if(this.isEllipsis)
					{
						if(!sAnno.getSName().equals("E") && !sAnno.getSName().contains("_E"))
							retAnnotation.setSName(sAnno.getSName()+"_E");
						sToken.addSAnnotation((SAnnotation)retAnnotation);
					}
				}
			}
		}
	}

	
	private LinkedList<String> h_removeOpenToken(LinkedList<String> list, String item)
	{
		LinkedList<String> ret = new LinkedList<String>();
		
		for(String str : list)
		{
			if(!str.equals(item))
				ret.add(str);
		}
		return ret;
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
