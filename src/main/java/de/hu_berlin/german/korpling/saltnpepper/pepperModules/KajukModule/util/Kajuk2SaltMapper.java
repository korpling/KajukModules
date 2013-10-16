package de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.osgi.service.log.LogService;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import de.hu_berlin.german.korpling.saltnpepper.pepper.pepperModules.MAPPING_RESULT;
import de.hu_berlin.german.korpling.saltnpepper.pepper.pepperModules.impl.PepperMapperImpl;
import de.hu_berlin.german.korpling.saltnpepper.pepperModules.KajukModule.exceptions.KajukImporterException;
import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;



public class Kajuk2SaltMapper extends PepperMapperImpl
{
	
	public static final String UTF8_BOM = "\uFEFF";
	
	/**
	 * {@inheritDoc PepperMapper#setSDocument(SDocument)}
	 * 
	 * OVERRIDE THIS METHOD FOR CUSTOMIZED MAPPING.
	 */
	@Override
	public MAPPING_RESULT mapSDocument() {	
		getSDocument().setSName(getSDocument().getSName());
		
		if (getSDocument().getSDocumentGraph()== null)
			getSDocument().setSDocumentGraph(SaltFactory.eINSTANCE.createSDocumentGraph());
		try
		{
			File file = new File(this.getResourceURI().toFileString());
			SAXParser parser;
			XMLReader xmlReader;
			SAXParserFactory factory= SAXParserFactory.newInstance();
			
			try
			{	
				parser= factory.newSAXParser();
				xmlReader = parser.getXMLReader();
				xmlReader.setContentHandler(new KajukContentHandler(getSDocument()));
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
					xmlReader.setContentHandler(new KajukContentHandler(getSDocument()));
					xmlReader.parse(file.getAbsolutePath());
				}
				catch (Exception e1) 
				{
		             throw new KajukImporterException("Cannot load Kajuk from resource '"+file.getAbsolutePath()+"'.", e1);
				}
			}
			getLogService().log(LogService.LOG_DEBUG, "SDocument '"+ getSDocument().getSName()+"' processed");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return(MAPPING_RESULT.FINISHED);
	}
}
