/*
 * Copyright 2002-2012 the original author or authors.
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

package com.rabbitframework.core.springframework.io;
import com.rabbitframework.core.springframework.util.Assert;
import com.rabbitframework.core.springframework.util.SpringStringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * {@link Resource} implementation for {@code java.io.File} handles.
 * Obviously supports resolution as File, and also as URL.
 * Implements the extended {@link WritableResource} interface.
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see File
 */
public class FileSystemResource extends AbstractResource implements WritableResource {

	private final File file;

	private final String path;


	/**
	 * Create a new FileSystemResource from a File handle.
	 * <p>Note: When building relative resources via {@link #createRelative},
	 * the relative path will apply <i>at the same directory level</i>:
	 * e.g. new File("C:/dir1"), relative path "dir2" -> "C:/dir2"!
	 * If you prefer to have relative paths built underneath the given root
	 * directory, use the {@link #FileSystemResource(String) constructor with a file path}
	 * to append a trailing slash to the root path: "C:/dir1/", which
	 * indicates this directory as root for all relative paths.
	 * @param file a File handle
	 */
	public FileSystemResource(File file) {
		Assert.notNull(file, "File must not be null");
		this.file = file;
		this.path = SpringStringUtils.cleanPath(file.getPath());
	}

	/**
	 * Create a new FileSystemResource from a file path.
	 * <p>Note: When building relative resources via {@link #createRelative},
	 * it makes a difference whether the specified resource base path here
	 * ends with a slash or not. In the case of "C:/dir1/", relative paths
	 * will be built underneath that root: e.g. relative path "dir2" ->
	 * "C:/dir1/dir2". In the case of "C:/dir1", relative paths will apply
	 * at the same directory level: relative path "dir2" -> "C:/dir2".
	 * @param path a file path
	 */
	public FileSystemResource(String path) {
		Assert.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = SpringStringUtils.cleanPath(path);
	}

	/**
	 * Return the file path for this resource.
	 */
	public final String getPath() {
		return this.path;
	}


	/**
	 * This implementation returns whether the underlying file exists.
	 * @see File#exists()
	 */
	@Override
	public boolean exists() {
		return this.file.exists();
	}

	/**
	 * This implementation checks whether the underlying file is marked as readable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see File#canRead()
	 * @see File#isDirectory()
	 */
	@Override
	public boolean isReadable() {
		return (this.file.canRead() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileInputStream for the underlying file.
	 * @see FileInputStream
	 */
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	/**
	 * This implementation returns a URL for the underlying file.
	 * @see File#toURI()
	 */
	@Override
	public URL getURL() throws IOException {
		return this.file.toURI().toURL();
	}

	/**
	 * This implementation returns a URI for the underlying file.
	 * @see File#toURI()
	 */
	@Override
	public URI getURI() throws IOException {
		return this.file.toURI();
	}

	/**
	 * This implementation returns the underlying File reference.
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * This implementation returns the underlying File's length.
	 */
	@Override
	public long contentLength() throws IOException {
		return this.file.length();
	}

	/**
	 * This implementation creates a FileSystemResource, applying the given path
	 * relative to the path of the underlying file of this resource descriptor.
	 * @see com.rapid.commons.spring.util.SpringStringUtils#applyRelativePath(String, String)
	 */
	@Override
	public Resource createRelative(String relativePath) {
		String pathToUse = SpringStringUtils.applyRelativePath(this.path, relativePath);
		return new FileSystemResource(pathToUse);
	}

	/**
	 * This implementation returns the name of the file.
	 * @see File#getName()
	 */
	@Override
	public String getFilename() {
		return this.file.getName();
	}

	/**
	 * This implementation returns a description that includes the absolute
	 * path of the file.
	 * @see File#getAbsolutePath()
	 */
	public String getDescription() {
		return "file [" + this.file.getAbsolutePath() + "]";
	}


	// implementation of WritableResource

	/**
	 * This implementation checks whether the underlying file is marked as writable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see File#canWrite()
	 * @see File#isDirectory()
	 */
	public boolean isWritable() {
		return (this.file.canWrite() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileOutputStream for the underlying file.
	 * @see FileOutputStream
	 */
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(this.file);
	}


	/**
	 * This implementation compares the underlying File references.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this ||
			(obj instanceof FileSystemResource && this.path.equals(((FileSystemResource) obj).path)));
	}

	/**
	 * This implementation returns the hash code of the underlying File reference.
	 */
	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

}
