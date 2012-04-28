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
- refresh the local repository more efficiently (don't re-read everything each time)


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




implement addon for:
- bayfiles: http://bayfiles.com/api
- google drive
- skydrive (msft)
- ftp
- webdav
- local copy


directory walker: http://commons.apache.org/io/api-release/org/apache/commons/io/DirectoryWalker.html
folder watch: http://commons.apache.org/io/api-release/index.html?org/apache/commons/io/monitor/package-summary.html


http://stackoverflow.com/questions/2185734/recursive-n-way-merge-diff-algorithm-for-directory-trees

packaging for Windows:
- http://winrun4j.sourceforge.net
- http://jsmooth.sourceforge.net