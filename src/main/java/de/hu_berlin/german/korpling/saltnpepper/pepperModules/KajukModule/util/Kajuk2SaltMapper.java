package de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.exceptions.KajukImporterException;
import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.SaltProject;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SCorpus;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SCorpusGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SMetaAnnotation;



public class Kajuk2SaltMapper 
{
	
	public static final String UTF8_BOM = "\uFEFF";
	
	
	private File corpusPath= null; 
	
	public File getCorpusPath() {
		return corpusPath;
	}
	public void setCorpusPath(File corpusPath) {
		this.corpusPath = corpusPath;
	}
	public Kajuk2SaltMapper()
	{
	}
	public void start(SDocument sDocument)
	{
		sDocument.setSName(sDocument.getSName()+".xml");
		SaltProject saltProject = SaltFactory.eINSTANCE.createSaltProject();

		SCorpusGraph sCorpusGraph = SaltFactory.eINSTANCE.createSCorpusGraph();
		saltProject.getSCorpusGraphs().add(sCorpusGraph);
		
		SCorpus sCorpus = SaltFactory.eINSTANCE.createSCorpus();
		sCorpus.setSName("KAJUK");
		sCorpusGraph.addNode(sCorpus);
		
		SDocument sDoc = null;
		
		sDoc = SaltFactory.eINSTANCE.createSDocument();
		sDoc.setSName(sDocument.getSName()+".xml");
		
		sCorpusGraph.addSDocument(sCorpus, sDoc);

		
		sDocument.setSDocumentGraph(SaltFactory.eINSTANCE.createSDocumentGraph());
		try
		{
			File file = new File(this.getCorpusPath().getAbsolutePath()+"/" + sDocument.getSName());
			SAXParser parser;
			XMLReader xmlReader;
			SAXParserFactory factory= SAXParserFactory.newInstance();
			
			try
			{	
				parser= factory.newSAXParser();
				xmlReader = parser.getXMLReader();
				xmlReader.setContentHandler(new KajukContentHandler(sDocument));
			}
			catch(ParserConfigurationException e)
			{
				throw new KajukImporterException("Cannot load Kajuk from resource '"+file.getAbsolutePath()+"'.",e);
			}
			catch(Exception e)
			{
				throw new KajukImporterException("Cannot load Kajuk from resource '"+file.getAbsolutePath()+"'.",e);
			}
			
			try 
			{
		         InputStream inputStream= new FileInputStream(file);
		         Reader reader = new InputStreamReader(inputStream, "UTF-8");
		    
		         InputSource is = new InputSource(reader);
		         is.setEncoding("UTF-8");
		    
		         xmlReader.parse(is);
			} 
			catch (SAXException e) 
			{
				try
				{
					parser= factory.newSAXParser();
					xmlReader= parser.getXMLReader();
					xmlReader.setContentHandler(new KajukContentHandler(sDocument));
					xmlReader.parse(file.getAbsolutePath());
				}
				catch (Exception e1) 
				{
		             throw new KajukImporterException("Cannot load Kajuk from resource '"+file.getAbsolutePath()+"'.", e1);
				}
			}
			
			System.out.println("\nDEBUG\n"+sDocument.getSName()+"\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
		
	
}
