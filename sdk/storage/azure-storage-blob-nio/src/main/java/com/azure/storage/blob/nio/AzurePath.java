// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio;

import com.azure.core.util.logging.ClientLogger;
import com.azure.storage.blob.nio.implementation.util.Utility;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.Stack;

/**
 * {@inheritDoc}
 */
public final class AzurePath implements Path {
    private final ClientLogger logger = new ClientLogger(AzurePath.class);
    private static final String ROOT_DIR_SUFFIX = ":";

    private final AzureFileSystem parentFileSystem;
    private final String pathString;

    AzurePath(AzureFileSystem parentFileSystem, String s, String... strings) {
        this.parentFileSystem = parentFileSystem;
        this.pathString = String.join(this.parentFileSystem.getSeparator(),
            // Strip any trailing or leading delimiters so there are no duplicates when we join.
            Flux.fromArray(s.split(this.parentFileSystem.getSeparator()))
                .concatWith(Flux.fromArray(strings)
                    .flatMap(str -> Flux.fromArray(str.split(this.parentFileSystem.getSeparator()))))
                .filter(str -> !str.isEmpty())
                .toIterable());
        if (this.pathString.contains(ROOT_DIR_SUFFIX)
            && (this.pathString.indexOf(ROOT_DIR_SUFFIX) >
            this.pathString.indexOf(this.parentFileSystem.getSeparator()))) {
            throw Utility.logError(logger, new InvalidPathException(this.pathString, ROOT_DIR_SUFFIX +
                " is an invalid character except to identify the root element of this path if there is one."));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileSystem getFileSystem() {
        return this.parentFileSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAbsolute() {
        return this.getRoot() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getRoot() {
        /*
        Check if the first element of the path is formatted like a root directory and if the name of the directory
        matches one of the file stores. (No directory should be formatted like a root dir and not match a file store
        name, but we validate just in case.)
         */
        String firstElement = pathString.split(parentFileSystem.getSeparator())[0];
        if (firstElement.endsWith(ROOT_DIR_SUFFIX)) {
            String fileStoreName = firstElement.substring(0, firstElement.length() - 1);
            Boolean validRootName = Flux.fromIterable(parentFileSystem.getFileStores())
                .map(FileStore::name)
                .hasElement(fileStoreName)
                .block();
            if (validRootName != null && validRootName) {
                return this.parentFileSystem.getPath(firstElement + this.parentFileSystem.getSeparator());
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getFileName() {
        if (this.withoutRoot().isEmpty()) {
            return null;
        } else {
            return this.parentFileSystem.getPath(
                Flux.fromArray(this.splitToElements()).last().block());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getParent() {
        /*
        If this path only has one element, there is no parent. Note the root is included in the parent, so we don't
        use getNameCount here.
         */
        if (this.splitToElements().length == 1) {
            return null;
        }

        /*
        This method may seem a bit circuitous, but using the javadocs define the behavior of this method in terms of
        the subpath method, so this is the best way to guarantee correctness.
         */
        Path parentNoRoot = this.subpath(0, getNameCount() - 1);
        Path root = getRoot();
        if (root != null) {
            return this.parentFileSystem.getPath(root.toString() + parentNoRoot.toString());
        }
        return parentNoRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNameCount() {
        return this.withoutRoot().split(this.parentFileSystem.getSeparator()).length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getName(int i) {
        if (i < 0 || i >= this.getNameCount()) {
            throw new IllegalArgumentException();
        }
        return this.parentFileSystem.getPath(this.withoutRoot().split(this.parentFileSystem.getSeparator())[i]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path subpath(int begin, int end) {
        if (begin < 0 || begin >= this.getNameCount()
            || end <= begin || end > this.getNameCount()) {
            throw new IllegalArgumentException();
        }

        Iterable<String> subnames = Flux.fromArray(this.withoutRoot().split(this.parentFileSystem.getSeparator()))
            .skip(begin)
            .take(end - begin)
            .toIterable();

        return this.parentFileSystem.getPath(String.join(this.parentFileSystem.getSeparator(), subnames));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startsWith(Path path) {
        /*
        There can only be one instance of a file system with a given id, so comparing object identity is equivalent
        to checking ids here.
         */
        if (path.getFileSystem() != this.parentFileSystem) {
            return false;
        }

        String[] thisPathElements = this.splitToElements();
        String[] otherPathElements = ((AzurePath) path).pathString.split(this.parentFileSystem.getSeparator());
        if (otherPathElements.length > thisPathElements.length) {
            return false;
        }
        for (int i = 0; i < otherPathElements.length; i++) {
            if (!otherPathElements[i].equals(thisPathElements[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startsWith(String s) {
        return this.startsWith(this.parentFileSystem.getPath(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean endsWith(Path path) {
        /*
        There can only be one instance of a file system with a given id, so comparing object identity is equivalent
        to checking ids here.
         */
        if (path.getFileSystem() != this.parentFileSystem) {
            return false;
        }

        String[] thisPathElements = this.splitToElements();
        String[] otherPathElements = ((AzurePath) path).pathString.split(this.parentFileSystem.getSeparator());
        if (otherPathElements.length > thisPathElements.length) {
            return false;
        }
        // If the given path has a root component, the paths must be equal.
        if (path.getRoot() != null && otherPathElements.length != thisPathElements.length) {
            return false;
        }
        for (int i = 1; i <= otherPathElements.length; i++) {
            if (!otherPathElements[otherPathElements.length - i]
                .equals(thisPathElements[thisPathElements.length - i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean endsWith(String s) {
        return this.endsWith(this.parentFileSystem.getPath(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path normalize() {
        Stack<String> stack = new Stack<>();
        String[] pathElements = this.splitToElements();
        Path root = this.getRoot();
        String rootStr = root == null ? null : root.toString();
        rootStr = rootStr == null ? null : rootStr.substring(0, rootStr.length() -1); // Strip the '/' for comparison.
        for (String element : pathElements) {
            if (element.equals(".")) {
                continue;
            } else if (element.equals("..")) {
                if (rootStr != null) {
                    // Root path. We never push "..".
                    if (stack.peek().equals(rootStr)) {
                        // Cannot go higher than root. Ignore.
                        continue;
                    } else {
                        stack.pop();
                    }
                } else {
                    // Relative paths can have an arbitrary number of ".." at the beginning.
                    if (stack.empty()) {
                        stack.push(element);
                    } else if (stack.peek().equals("..")) {
                        stack.push(element);
                    } else {
                        stack.pop();
                    }
                }
            } else {
                stack.push(element);
            }
        }

        return this.parentFileSystem.getPath(String.join(this.parentFileSystem.getSeparator(), stack));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolve(Path path) {
        if (path.isAbsolute()) {
            return path;
        }
        if (path.getNameCount() == 0) {
            return this;
        }
        return this.parentFileSystem.getPath(this.toString(), path.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolve(String s) {
        return this.resolve(this.parentFileSystem.getPath(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolveSibling(Path path) {
        if (path.isAbsolute()) {
            return path;
        }

        Path parent = this.getParent();
        return parent == null ? path : parent.resolve(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolveSibling(String s) {
        return this.resolveSibling(this.parentFileSystem.getPath(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path relativize(Path path) {
        if (path.getRoot() == null ^ this.getRoot() == null) {
            throw Utility.logError(logger,
                new IllegalArgumentException("Both paths must be absolute or neither can be"));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI toUri() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path toAbsolutePath() {
        return null;
    }

    /**
     * Unsupported.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Path toRealPath(LinkOption... linkOptions) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File toFile() {
        return null;
    }

    /**
     * Unsupported.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kinds, WatchEvent.Modifier... modifiers) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kinds) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Path> iterator() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Path path) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.pathString;
    }

    private String withoutRoot() {
        Path root = this.getRoot();
        if (root != null) {
            return this.pathString.substring(root.toString().length());
        }

        return this.pathString;
    }

    private String[] splitToElements() {
        return this.pathString.split(this.parentFileSystem.getSeparator());
    }
}
