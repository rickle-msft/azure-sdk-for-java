# Release History

## 12.0.0-beta.3 (Unreleased)
- Added support for FileSystemProvider.checkAccess method
- Added support for file key on AzureBasicFileAttributes and AzureBlobFileAttributes
- Operations on a file system after it is closed now throw ClosedFileSystemException instead of IOException
- Added a minimal implementation of SeekableByteChannel

## 12.0.0-beta.2 (2020-08-13)
- Added checks to ensure file system has not been closed before operating on data

## 12.0.0-beta.1 (2020-07-17)
- Initial Release. Please see the README for more information.
