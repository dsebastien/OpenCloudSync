9f932ed8105524e91a78ec9e4d4707316ed8f08d

9f  <-- folder
	93  <-- folder
		2e  <-- folder
			d8  <-- folder
				10  <-- folder
					55  <-- folder
						24  <-- folder
							e9  <-- folder
								1a  <-- folder
									78  <-- folder
										ec  <-- folder
											9e4d4707316ed8f08d  <-- file

9f93
	2ed8
		1055
			24e9
				1a78
					ec9e
						4d4707316ed8f08d

9f932e
	d81055
		24e91a
			78ec9e
				4d4707
					316ed8f08d

9f932ed8
	105524e9
		1a78ec9e
			4d470731
				6ed8f08d













todo pom.xml
------------
- add/configure maven license plugin

stuff to think about
--------------------
- possible to make the max file chunk size vary depending on the file size? Bigger chunk size might be great for performance on larger files
    --> check file size & fix a chunk size depending on that
    --> if chunks are bigger
        - faster reads on large files
        - more disk space required (less deduplication)
        - more memory usage



ByteProcessor -> calculate the checksums for chunks & create the FileChunk objects
---> processBytes(...)

ByteStreams.read
.getDigest
.getCheckum

readBytes(inputSupplier, processor)
length -> length in bytes of the given stream




FileSystemWatcher:
IndexManager
Index

RepositoryManager
Repository

StorageManager -> notifies:
DropBoxStorageManager


Q:
- Checksum on whole files also?
- Encryption, at which stage?





implement hashCode
traverse both trees simultaneously
http://stackoverflow.com/questions/761520/fastest-way-to-compare-two-data-structures-in-java
http://en.wikipedia.org/wiki/Tree_traversal


jdk 7: http://openjdk.java.net/projects/nio/javadoc/java/nio/file/WatchService.html
http://docs.oracle.com/javase/tutorial/essential/io/notification.html
http://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java




alternatives:
http://commons.apache.org/jci/commons-jci-fam/index.html
http://jpoller.sourceforge.net/
http://jnotify.sourceforge.net/



read: http://javabeanz.wordpress.com/2007/07/13/treemap-vs-hashmap/



http://docs.oracle.com/javase/6/docs/api/javax/swing/tree/TreeModel.html
http://docs.oracle.com/javase/6/docs/api/javax/swing/tree/TreeNode.html




http://www.bouncycastle.org/wiki/display/JA1/Elliptic+Curve+Key+Pair+Generation+and+Key+Factories




implement addon for bayfiles: http://bayfiles.com/api




directory walker: http://commons.apache.org/io/api-release/org/apache/commons/io/DirectoryWalker.html
folder watch: http://commons.apache.org/io/api-release/index.html?org/apache/commons/io/monitor/package-summary.html


http://stackoverflow.com/questions/2185734/recursive-n-way-merge-diff-algorithm-for-directory-trees

packaging for Windows:
- http://winrun4j.sourceforge.net
- http://jsmooth.sourceforge.net