package jlm.core.ui;

/*BEGIN_COPYRIGHT_BLOCK
 *
 * Copyright (c) 2001-2008, JavaPLT group at Rice University (drjava@rice.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the names of DrJava, the JavaPLT group, Rice University, nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software is Open Source Initiative approved Open Source Software.
 * Open Source Initative Approved is a trademark of the Open Source Initiative.
 * 
 * This file is part of DrJava.  Download the current version of this project
 * from http://www.drjava.org/ or http://sourceforge.net/projects/drjava/
 * 
 * END_COPYRIGHT_BLOCK*/

import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.io.*;

/**
 * Writes manifest objects. Useful for creating Manifest files without writing
 * them to files.
 */
public class ManifestWriter
{
	private List<String> _classPaths;
	private String _mainClass;
	private String _rawManifest;
	public static final Manifest DEFAULT = new ManifestWriter().getManifest();

	/** Create a new manifest file */
	public ManifestWriter()
	{
		_classPaths = new LinkedList<String>();
		_mainClass = null;
		_rawManifest = null;
	}

	/**
	 * Add a class path to the Manifest
	 * 
	 * @param path
	 *            the path to be added
	 */
	public void addClassPath(String path)
	{
		_classPaths.add(_classPaths.size(), path);
	}

	/**
	 * Set the main class of the Manifest
	 * 
	 * @param mainClass
	 */
	public void setMainClass(String mainClass)
	{
		_mainClass = mainClass;
		_rawManifest = null;
	}

	public void setManifestContents(String rawManifest)
	{
		_rawManifest = rawManifest;
		_mainClass = null;
	}

	/**
	 * Get an input stream to the contents of the manifest file
	 * 
	 * @return an InputStream whose contents are the contents of the Manifest
	 *         file
	 */
	protected InputStream getInputStream()
	{
		// NOTE: All significant lines in the manifest MUST end in the end of line character

		final StringBuilder sbuf = new StringBuilder();
		sbuf.append(Attributes.Name.MANIFEST_VERSION.toString());
		sbuf.append(": 1.0" + "\n");
		if (!_classPaths.isEmpty())
		{
			Iterator<String> iter = _classPaths.iterator();
			sbuf.append(Attributes.Name.CLASS_PATH.toString());
			sbuf.append(":");
			while (iter.hasNext())
			{
				sbuf.append(" ");
				sbuf.append(iter.next());
			}
			sbuf.append("\n");
		}
		if (_mainClass != null)
		{
			sbuf.append(Attributes.Name.MAIN_CLASS.toString());
			sbuf.append(": ");
			sbuf.append(_mainClass);
			sbuf.append("\n");
		}

		if (_rawManifest != null)
		{
			sbuf.append(_rawManifest);

			if (!_rawManifest.endsWith("\n"))
				sbuf.append("\n");
		}

		try
		{
			return new ByteArrayInputStream(sbuf.toString().getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the Manifest object that this object created.
	 * 
	 * @return the Manifest that this builder created
	 */
	public Manifest getManifest()
	{
		try
		{
			Manifest m = new Manifest();
			m.read(getInputStream());
			return m;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}