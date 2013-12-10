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

import org.osgi.service.log.LogService;

import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAbstractAnnotation;

public abstract class ConversionClass {

	
	public static void convert(SAbstractAnnotation sAnno, String elementName, String attrName, String attrValue, LogService logService)
	{
		if(attrName == null || attrValue == null)
		{
			if(elementName.equals("pb"))
			{
				return;
			}
			else if(elementName.equals("line"))	 
			{
				return;
			}
			else if(elementName.equals("lb"))
			{
				sAnno.setName("lb");
				sAnno.setValue("lb");
				return;
			}
			else if(elementName.equals("VOR"))
			{
				sAnno.setName("Vorfeld");
				sAnno.setValue("VOR");
				return;
			}
			else if(elementName.equals("praed"))
			{
				sAnno.setName("satzglied");
				sAnno.setValue("praed");
				return;
			}
			else if(elementName.equals("V"))
			{
				sAnno.setName("V");
				sAnno.setValue("V");
				return;
			}
			else if(elementName.equals("VV"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue("VV");
				return;
			}
			else if(elementName.equals("VV"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue("VV");
				return;
			}	
			else if(elementName.equals("subj"))
			{
				sAnno.setName("satzglied");
				sAnno.setValue("subj");
				return;
			}
			else if(elementName.equals("AcI"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("AD"))
			{
				sAnno.setName("J_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("ADJGr"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("ADV"))
			{
				sAnno.setName("satzglied");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("akk"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("AP"))
			{
				sAnno.setName("J_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("FOK"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("FV"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("HMV"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("HV"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("J"))
			{
				sAnno.setName("J");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("KON"))
			{
				sAnno.setName("J_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("KOR"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("KV"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("LASSEN"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("MV"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("NGr"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("obj"))
			{
				sAnno.setName("satzglied");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("akk"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("obl"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("prae"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("obj"))
			{
				sAnno.setName("satzglied");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("PGr"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("Phras"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("RF"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("SUB"))
			{
				sAnno.setName("J_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("TUN"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("VFin"))
			{
				sAnno.setName("pos_ID");
				sAnno.setValue("Fin");
				return;
			}
			else if(elementName.equals("VP"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("doc"))
			{
				return;
			}
			else if(elementName.equals("PV"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("XX"))
			{
				sAnno.setName("X");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("KOR"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("Inf"))
			{
				sAnno.setName("pos_ID");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("IP"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue(elementName);
				return;
			}
			else if(elementName.equals("newline"))
			{
				return;
			}
			else if(elementName.equals("newpage"))
			{
				return;
			}
			else
			{
				logService.log(LogService.LOG_WARNING, "Unknown tag without attributes ("+elementName+")");
			}
		}
		else
		{
			if(elementName.equals("line"))
			{
				return;
			}
			else if(elementName.equals("doc"))
			{
				return;
			}
			else if(elementName.equals("pb"))
			{
				return;
			}
			else if(elementName.equals("lb"))
			{
				if(attrName.equals("n"))	
				{
					sAnno.setName(elementName+"_n");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName("type_lb");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDtype"))
				{
					sAnno.setName("ADDtype_lb");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("IR"))
				{
					sAnno.setName(attrName+"_lb");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDIR"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("EB"))
				{
					sAnno.setName("EB");
					sAnno.setValue(attrValue);
					return;
				}
				else 
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("AP"))
			{
				if(attrName.equals("type") && attrValue.equals("FOK"))
				{
					sAnno.setName("pos_KAJUK");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName(elementName+"_"+attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDtype"))
				{
					if(attrValue.equals("x"))
					{
						sAnno.setName("X");
						sAnno.setValue("x");
						return;
					}
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else 
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("praed"))
			{
				if(attrName.equals("real"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName(elementName+"_"+attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("VOR"))
			{
				if(attrName.equals("real"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName(elementName+"_"+attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("SUB"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName("satzglied");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDIR"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("IR"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ID"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("norm"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDtype"))
				{
					if(attrValue.equals("x"))
					{
						sAnno.setName("X");
						sAnno.setValue("x");
						return;
					}
					sAnno.setName(attrValue);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("obj"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("V"))
			{
				if(attrName.equals("ID"))
				{
					sAnno.setName("pos_ID");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else 
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("KOR"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDtype"))
				{
					sAnno.setName("ADDtype");
					sAnno.setValue("E");
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName("dir");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName("satzglied_type");
					sAnno.setValue(attrValue);
					return;
				}
				else 
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("FOK"))
			{
				if(attrName.equals("norm"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else 
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("KON"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else 
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("VV"))
			{
				sAnno.setName("pos_KAJUK");
				sAnno.setValue("VV");
				return;
			}
			else if(elementName.equals("VV"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue("VV");
				return;
			}	
			else if(elementName.equals("subj"))
			{
				if(attrName.equals("real"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName("satzglied_type");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ADDtype"))
				{
					sAnno.setName(attrValue);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("HV"))
			{
				sAnno.setName("satzglied_type");
				sAnno.setValue("HV");
				return;
			}
			else if(elementName.equals("J"))
			{
				if(attrName.equals("ADDIR"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("EB"))
				{
					sAnno.setName("EB");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("norm"))
				{
					sAnno.setName("norm");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("ID"))
				{
					sAnno.setName("J_ID");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("IR"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("norm"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					if(attrValue.equals("x"))
					{
						sAnno.setName("X");
						sAnno.setValue("x");
						return;
					}
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("XX"))
			{
				if(attrName.equals("ID"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("ADV"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("change"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("IP"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("newline"))
			{
				if(attrName.equals("n"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("newpage"))
			{
				if(attrName.equals("n"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("RF"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName("E");
					sAnno.setValue(attrValue);
					return;
				}
				else if(attrName.equals("dir"))
				{
					sAnno.setName("dir");
					sAnno.setValue(attrValue);
					return;
				}
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals(""))
			{
				if(attrName.equals("norm"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("Inf"))
			{
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				if(attrName.equals("dir"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else if(elementName.equals("AD"))
			{
				if(attrName.equals("norm"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				if(attrName.equals("type"))
				{
					sAnno.setName(attrName);
					sAnno.setValue(attrValue);
					return;
				}
				else
					logService.log(LogService.LOG_WARNING, "No handler for "+ elementName+"_"+attrName);
			}
			else
			{
				logService.log(LogService.LOG_WARNING, "Unknown tag with attributes ("+elementName+")");
			}
		}
	}
	
	
}
