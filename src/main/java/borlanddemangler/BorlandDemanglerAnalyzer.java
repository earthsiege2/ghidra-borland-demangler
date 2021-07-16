/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package borlanddemangler;

import ghidra.app.plugin.core.analysis.AbstractDemanglerAnalyzer;
import ghidra.app.util.demangler.DemangledException;
import ghidra.app.util.demangler.DemangledObject;
import ghidra.app.util.demangler.DemangledUnknown;
import ghidra.app.util.demangler.DemanglerOptions;
import ghidra.app.util.importer.MessageLog;
import ghidra.app.util.opinion.PeLoader;
import ghidra.program.model.lang.CompilerSpec;
import ghidra.program.model.listing.Program;
/**
 * De-mangle Borland label names
 * 
 * Sources used:
 * - Calling conventions for different C++ compilers and operating systems by Agner Fog, TU Denmark - https://www.agner.org/optimize/calling_conventions.pdf
 * 
 */
public class BorlandDemanglerAnalyzer extends AbstractDemanglerAnalyzer {
	private static final String NAME="Demangler Borland";
	private static final String DESCRIPTION="This analyzer will attempt to de-mangle Borland names";
	public BorlandDemanglerAnalyzer() {
		super(NAME,DESCRIPTION);
	}

	@Override
	public boolean canAnalyze(Program program) {
		String executableFormat = program.getExecutableFormat();
		if(executableFormat ==null || !executableFormat.equals(PeLoader.PE_NAME)) {
			System.err.println("WARNING: Cannot analyze program of type "+String.valueOf(executableFormat));
			return false;
		}
		CompilerSpec spec = program.getCompilerSpec();
		String specId = spec.getCompilerSpecID().getIdAsString();
		if(specId==null|| !specId.startsWith("borland")) {
			System.err.println("WARNING: Compiler ID "+String.valueOf(specId)+" does not look like Borland");
			return false;
		}
		return true;
	}
	@Override
	protected DemangledObject doDemangle(String mangled, DemanglerOptions options, MessageLog log)
			throws DemangledException {
		log.appendMsg("Attempting to de-mangle "+mangled);
		String name="";
		DemangledObject ret=null;
		if(mangled.startsWith("_")) { //easiest case: global object, no class/namespace qualifier, not mangled
			name=mangled.substring(1);
			ret=new DemangledUnknown(mangled, "", name);
			log.appendMsg("De-mangled "+mangled+" into global object "+name);
		} else if(mangled.startsWith("@@%")) { //Template stuff
			throw new DemangledException("Unable to digest template name "+mangled);
		} else if(mangled.startsWith("@@") && mangled.endsWith("@3")) { //vtables
			throw new DemangledException("Unable to digest vtable "+mangled);
		} else if(mangled.startsWith("@@")) { //Barf
			throw new DemangledException("Unable to digest "+mangled+", type unknown");
		} else if(mangled.startsWith("@")) { //Anything else with a class and/or namespace prefix
			
		} else { //Anything else... entry point, abort point, resource tables
			//Leave me to whoever else wants to lay a claim
		}
		return ret;
	}

}
