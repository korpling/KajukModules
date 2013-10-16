package de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util;



import java.util.Collection;

import de.hu_berlin.german.korpling.saltnpepper.salt.graph.Node;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.tokenizer.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale.*;

import javax.security.auth.callback.LanguageCallback;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
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
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.STextualRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SToken;
//import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.tokenizer.Tokenizer;
//import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.tokenizer.*;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAbstractAnnotation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAnnotation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SLayer;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SNode;

public class KajukContentHandler extends DefaultHandler2 KajukProperties implements ContentHandler 
{

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
	private boolean withinText;
	private StringBuilder stringBuilder;
	private String realString;
	private int tokNumber;
	
	private Tokenizer tokenizer;
	
	private SLayer junktionen;
	private SLayer lexikalische_annotationen;
	private SLayer syntaktische_annotationen;
	private SLayer ellipsenannotationen;
	private SLayer sachverhaltsdarstellungen;
	private SLayer graphische_annotationen;
	private SLayer hilfsannotationen;
	
	
	// this is the hashmap for the annotations 
	private HashMap<String, EList<SAbstractAnnotation>> annotationMap;
	
	public KajukContentHandler(SDocument sDocument)
	{
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
		this.withinText = false;
		this.realString = "";
		this.stringBuilder = new StringBuilder();
		this.tokNumber = 1;
		
		this.tokenizer = new Tokenizer();
		
		this.junktionen 					= SaltFactory.eINSTANCE.createSLayer();
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
	 * This method returns all read characters. What we do is the following: 
	 * At first we get the text so far by currentSDS.getSText(), which will be appended to a new textbuffer. With this, we are 
	 * able to determine the start and end indexes of the new token. After the creation of the token, a STextualRelation between the token 
	 * and the STextualDS is created with the use of the sStart and sEnd indexes. 
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
	 * here we want to put every open tag onto a stack
	 */
	public void startElement(	String uri, 
								String localName, 
								String qName,
								Attributes attributes) 
			throws SAXException 
	{
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
				System.out.println(qName + " already in AnnotationMap, should not be there.. ");
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
				
				// This is the case that there already was a newline element
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
							//setting the span as source of the relation
							sSpanRel.setSSpan(sSpan);
							//setting the first token as target of the relation
							sSpanRel.setSToken(sToks);
							//adding the created relation to the document-graph
							sDocument.getSDocumentGraph().addSRelation(sSpanRel);
						}
					}
					this.openSpans.remove(qName);
				}
				else // this is the case if it is the first newline element evah
				{
					this.openSpans.put(qName, new LinkedList<SToken>());
					System.out.println("put "+qName+" to openSpans");
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
							//setting the span as source of the relation
							sSpanRel.setSSpan(sSpan);
							//setting the first token as target of the relation
							sSpanRel.setSToken(sToks);
							//adding the created relation to the document-graph
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
	
	public void endElement(	String uri, 
							String localName, 
							String qName)
			throws SAXException 
	{
			this.withinText = false;
			//this.stringBuilder = null;
			if(this.stringBuilder!=null && this.stringBuilder.toString().length() > 0)
			{
				this.realString = this.stringBuilder.toString();
				this.stringBuilder = null;
			}
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
				if(qName.equals("newline") || qName.equals("newpage"))
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
//						h_createRelationType(numbers,sSpan);
						sSpan.addSAnnotation(sAnno);
						if(qName.equals("lb"))
						{
							sAnno = SaltFactory.eINSTANCE.createSAnnotation();
							sAnno.setSName("lb");
							sAnno.setSValue("lb");
							sSpan.addSAnnotation(sAnno);
						}
						//h_copySAbstractAnnotations(sSpan);
						this.sDocument.getSDocumentGraph().addSNode(sSpan);
						//create spanning relations to all tokens
						for(SToken sToks : this.openSpans.get(qName)) 
						{
							SSpanningRelation sSpanRel= SaltFactory.eINSTANCE.createSSpanningRelation();
							//setting the span as source of the relation
							sSpanRel.setSSpan(sSpan);
							//setting the first token as target of the relation
							sSpanRel.setSToken(sToks);
							//adding the created relation to the document-graph
							sDocument.getSDocumentGraph().addSRelation(sSpanRel);
							
							
							
							//adding the annotation to the placeholder span
		//					sDocument.getSDocumentGraph().getSSpans().get(0).addSAnnotation(sAnno);
		//					
		//					sAnno= SaltFactory.eINSTANCE.createSAnnotation();
		//					sAnno.setSName("Inf-Struct");
		//					sAnno.setSValue("topic");
		//					sDocument.getSDocumentGraph().getSSpans().get(1).addSAnnotation(sAnno);
						}
					}
					this.openSpans.remove(qName);
				}
				
			}
			this.currentXPath.removeLastStep();
			
			if(this.annotationMap.get(qName) != null)
			{
				// TODO: maybe we have to distinguish the actual "lb" to be removed
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
				System.out.println(qName + " was already removed or never in the annotationMap");
			}
			
			// it is necessary to distinguish between span tags and newline/newpage, because
			// their usage differs. A line/page ends at the open tag of newline/newpage
			// and starts at the close tag
//			if(qName.equals("newline") || qName.equals("newpage"))
//			{
//				if(this.openSpans.get(qName) == null)
//					this.openSpans.put(qName, new LinkedList<SToken>());
//				else
//					System.out.println("newline/newpage already there");
//			}


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
	 * 
	 *  TODO: we want to create annotations also if no attributes are given. 
	 *  e.g. the element &lt;AD&gt;&lt;/AD&gt; equals J_type="AD" 
	 *  
	 * @param qName the current element
	 * @param attributes the attributes of the element, may be null 
	 * @return 
	 */
	private EList<SAbstractAnnotation> h_createSAbstractAnnotations(	String qName,
																		Attributes attributes)
	{
		//System.out.println("Element "+qName+" attributes: " + attributes.getLength());
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
					h_getProperAnnotations(sAnno, qName, null, null);
				}
				else
					h_getProperAnnotations(sAnno, qName, attributes.getQName(i),attributes.getValue(i));
				if(sAnno.getSName() == null && !qName.equals("doc") && !qName.equals("newline") && !qName.equals("newpage") && !qName.equals("line") && !qName.equals("pb"))
					System.out.println("WARNING: sAnno still null - qName = " + qName);
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
	 * this method creates the actual annotations
	 * @param sAnno the annotation object
	 * @param elementName the current element name
	 * @param attrName the attribute name
	 * @param attrValue the attribute value
	 */
	private void h_getProperAnnotations(SAbstractAnnotation sAnno, String elementName, String attrName, String attrValue)
	{
		ConversionClass.convert(sAnno, elementName, attrName, attrValue);
	}

	
//	private class ElementNodeEntry
//	{
//		public List<SNode> openSNodes=null;
//		
//		public String nodeName = null;
//		
//		public EList<SAbstractAnnotation> annotations = null;
//		
//		public Boolean isComplex = false;
////		public SToken representedSToken=null;
//		
////		public ElementNodeEntry( 	final String nodeName, 
////									final EList<SAbstractAnnotation> annotations)
////		{
////			this.nodeName = nodeName;
////			this.annotations = annotations;
////			this.openSNodes = new Vector<SNode>();
////		}
//		
////		public Boolean isComplex()
////		{
////			return isComplex;
////		}
////		public void setIsComplex(Boolean isComplex)
////		{
////			this.isComplex = isComplex;
////			if( 	isComplex &&
////					representedSToken == null)
////			{
////				if(!this.openSNodes.contains(representedSToken))
////					this.openSNodes.add(representedSToken);
////				
////				if(elementNodeStack.size()>1)
////					elementNodeStack.get(elementNodeStack.size()-2).openSNodes.remove(representedSToken);
////				representedSToken= null;
////			}
////		}
//		
//		public String toString()
//		{
//			return ("["+nodeName+", annotations: "+annotations+", isComplex: "+isComplex+", openSNodes: "+this.openSNodes+"]");
//		}
//	}
	
	
	public void endDocument() throws SAXException {
		int d = 0;
		for(String st : this.houseNumbers.keySet())
		{
			d += this.houseNumbers.get(st).size();
		}

		HashMap<String,LinkedList<SSpan>> tmp = new HashMap<String,LinkedList<SSpan>>();
		for(String str : this.houseNumbers.keySet())
		{
//			if(str.contains("a") || str.contains("b"))
//			{
//				LinkedList<SSpan> oldSpans = new LinkedList<SSpan>();
//				oldSpans = this.houseNumbers.get(str.subSequence(0, str.length()-1)); 
//				if(oldSpans != null)
//					oldSpans.addAll(this.houseNumbers.get(str));
//				else
//					oldSpans = this.houseNumbers.get(str);
//				if(str.trim().equals(""))
//					System.out.println("WARNING, putting \"\" in houseNumbers");
//				tmp.put(str.substring(0,str.length()-1), oldSpans);
//			}
		}
		for(String str : tmp.keySet())
		{
			this.houseNumbers.put(str, tmp.get(str));
		}
		
		for(String str : this.houseNumbers.keySet())
		{
//				System.out.println("\n\n"+str + " has " +this.houseNumbers.get(str).size()+ " spans\n");
			for(SSpan span : this.houseNumbers.get(str))
			{
//					for(SAnnotation sAnno : span.getSAnnotations())
//					{
//						if(sAnno != null && sAnno.getName() != null && sAnno.getName().equals("lb_n"))
//							System.out.println(sAnno.getName() + " \t-\t "+sAnno.getValueString());
//						
//					}
//					System.out.println("**************");
				int index=0;
				for(int i = 0; i < this.houseNumbers.get(str).size(); i++)
				{
					if(this.houseNumbers.get(str).get(i).equals(span))
						index = i+1;
				}
				
				if(index < this.houseNumbers.get(str).size())//  && (str.equals("1")|| str.equals("1a") || str.equals("3001") || str.equals("3003")))
				{
//						System.out.println("connecting " + this.houseNumbers.get(str).indexOf(span)+ " with " + index + "("+this.houseNumbers.get(str).size()+")");
//						System.out.println("\t"+span+" to " + this.houseNumbers.get(str).get(index));
					
					SPointingRelation sPointingRelation = SaltFactory.eINSTANCE.createSPointingRelation();
					sPointingRelation.setSSource(span);
					sPointingRelation.setSTarget(this.houseNumbers.get(str).get(index));
					System.out.println("connecting "+span.getIdentifier().getId() + " with " + this.houseNumbers.get(str).get(index).getIdentifier().getValueString() );
					System.out.println(str);
					if(span == this.houseNumbers.get(str).get(index))
						System.out.println("WARNING: pointing relation to itself!");
					//System.out.println("creating PR from "+span.getSAnnotation("lb_n").getValueString() + " to " + this.houseNumbers.get(str).get(index).getSAnnotation("lb_n").getValueString());
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
						System.out.println("WARNING: house number type is wrong!");
				}
				else
				{
//					if(this.houseNumbers.get(str+"a")!= null && !this.houseNumbers.get(str+"a").isEmpty())
//					{
//						System.out.println("get(str+a) == " + this.houseNumbers.get(str+"a"));
//						for(SSpan tmpSpan : this.houseNumbers.get(str+"a"))
//						{
//							SPointingRelation sPointingRelation = SaltFactory.eINSTANCE.createSPointingRelation();
//							sPointingRelation.setSSource(span);
//							sPointingRelation.setSTarget(tmpSpan);
//							sDocument.getSDocumentGraph().addSRelation(sPointingRelation);
//							String type = "";
//							int ret;
//							if(str.contains("a") || str.contains("b"))
//								ret = Integer.valueOf(str.substring(0, str.length()-1));
//							else
//								ret = Integer.valueOf(str);
//							if(ret >= 1 && ret <= 999)
//								type = "SubKon";
//							else if(ret >= 1000 && ret <= 1999)
//								type = "LatSub";
//							else if(ret >= 2000 && ret <= 2999)
//								type = "APAD";
//							else if(ret >= 3000)
//								type = "E";
//							if(!type.equals(""))
//								sPointingRelation.addSType(type);	
//							else
//								System.out.println("WARNING: house number type is wrong!");
//						}
//					}
//					else
//						System.out.println("this.housenumbers.get("+str+"a) == null!");
				}
				
				
			}
			
			
		}
		
		for(String str : this.houseNumbers.keySet())
		{
			if(str.contains("a") || str.contains("b"))
			{
				String get = str.substring(0,str.length()-1);
				System.out.println("\""+get+"\"");
				System.out.println(this.houseNumbers.keySet());
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
					System.out.println("WARNING: house number type is wrong!");
			}
		}
		
		sDocument.setSDocumentGraph(sDocumentGraph);
	}



	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}



	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
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
	
	private void h_copySAbstractAnnotations(SNode sNode)
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
					
					/* here we add the node directly to the corresponding layer */
					if(		sAnno.getSName().equals("J") ||
							sAnno.getSName().equals("ADDIR") ||
							sAnno.getSName().equals("IR") ||
							sAnno.getSName().equals("ID")
					)
					{
						this.junktionen.getSNodes().add(sNode);
					}
					
					if(		sAnno.getSName().equals("pos_KAJUK") ||
							sAnno.getSName().equals("pos_ID") ||
							sAnno.getSName().equals("change") 
					)
					{
						this.lexikalische_annotationen.getSNodes().add(sNode);
					}
					
					if(		sAnno.getSName().equals("satzglied") ||
							sAnno.getSName().equals("satzglied_type") ||
							sAnno.getSName().equals("real") ||
							sAnno.getSName().equals("Vorfeld")
					)
					{
						this.syntaktische_annotationen.getSNodes().add(sNode);
					}
					
					if(		sAnno.getSName().contains("_E")
					)
					{
						this.ellipsenannotationen.getSNodes().add(sNode);
					}
					
					if(		sAnno.getSName().contains("lb")
					)
					{
						this.sachverhaltsdarstellungen.getSNodes().add(sNode);
					}
					
					if(		sAnno.getSName().equals("line") ||
							sAnno.getSName().equals("pb") ||
							sAnno.getSName().equals("doc") 
					)
					{
						this.graphische_annotationen.getSNodes().add(sNode);
					}
					
					if(		sAnno.getSName().equals("XX") ||
							sAnno.getSName().equals("viol") ||
							sAnno.getSName().equals("norm") 
					)
					{
						this.hilfsannotationen.getSNodes().add(sNode);
					}
					
					
					
					if(this.isEllipsis)
					{
						if(!sAnno.getSName().equals("E") && !sAnno.getSName().contains("_E"))
							retAnnotation.setSName(sAnno.getSName()+"_E");
						sNode.addSAnnotation((SAnnotation)retAnnotation);
					}
					else
					{
						sNode.addSAnnotation((SAnnotation)retAnnotation);
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
	
	private void h_createRelationType(LinkedList<String> numbers, SSpan sSpan)
	{
		HashMap<String, String> annoList = new HashMap<String, String>();
		for(String nr : numbers)
		{
			try
			{
				int integerNr;
				if(nr.contains("a") || nr.contains("b"))
				{
					String ret = nr.substring(0, nr.length()-1).trim();
					if(!ret.equals(""))
						integerNr = Integer.valueOf(ret);
					else
						integerNr = -1;
				}
				else 
				{
					if(!nr.trim().equals(""))
						integerNr = Integer.valueOf(nr.trim());
					else
						integerNr = -1;
				}
				
				if(integerNr > 0 && integerNr <1000)
				{
					if(annoList.get("Hn0") != null)
						annoList.put("Hn0", annoList.get("Hn0") +","+ nr);
					else
						annoList.put("Hn0", nr);
				}
				else if(integerNr >= 1000 && integerNr < 2000)
				{
					if(annoList.get("Hn1") != null)
						annoList.put("Hn1", annoList.get("Hn1") +","+ nr);
					else
						annoList.put("Hn1", nr);
				}
				else if(integerNr >= 2000 && integerNr < 3000)
				{
					if(annoList.get("Hn2") != null)
						annoList.put("Hn2", annoList.get("Hn2") +","+ nr);
					else
						annoList.put("Hn2", nr);
				}
				else if(integerNr >= 3000)
				{
					if(annoList.get("Hn3") != null)
						annoList.put("Hn3", annoList.get("Hn3") +","+ nr);
					else
						annoList.put("Hn3", nr);
				}
				else if(integerNr == -1)
				{
					System.out.println("WARNING: got empty number, but that is not that important " + nr);
				}
				else
				{
					System.out.println("WARNING: got wrong number, that's bad! " + nr);
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("ERROR: NumberFormatException");
			}
		}
		
		for(String annoName : annoList.keySet())
		{
			SAnnotation sAnno = SaltFactory.eINSTANCE.createSAnnotation();
			sAnno.setSName(annoName);
			sAnno.setSValue(annoList.get(annoName));
			sSpan.addSAnnotation(sAnno);
		}
	}
	
}
