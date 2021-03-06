/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */
package prefux.data.io;

import java.io.File;
import java.io.OutputStream;

import prefux.data.Table;

/**
 * Interface for classes that write Table data to a particular file format.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface TableWriter {

    /**
     * Write a table to the file with the given filename.
     * @param table the Table to write
     * @param filename the file to write the table to
     * @throws DataWriteException
     */
    public void writeTable(Table table, String filename) throws DataIOException;
    
    /**
     * Write a table to the given File.
     * @param table the Table to write
     * @param f the file to write the table to
     * @throws DataWriteException
     */
    public void writeTable(Table table, File f) throws DataIOException;
    
    /**
     * Write a table from the given OutputStream.
     * @param table the Table to write
     * @param os the OutputStream to write the table to
     * @throws DataWriteException
     */
    public void writeTable(Table table, OutputStream os) throws DataIOException;
    
} // end of interface TableWriter
