- method to reconstruct a local folder based on the index information to check if correct
- problem if file contains the same file chunk twice or more?? unique constraint on file_has_filechunks table, how to avoid that?
- + what about the order??? (retrieve file chunks in the correct order
- add more logging output
- replace xml with annotations..
- dbschema, varchar(255) for file names? larger? impact on db size & performances...
- implement hashCode / equals
- write unit tests ;-)
- pom.xml: configure maven license plugin
- check if H2 cache settings useful: http://h2database.com/html/features.html#cache_settings
- proxy support
- options
    - proxy settings
    - bandwidth limits
        - per add-on?
    - cpu limits (?)
- app packaging
    windows:
        - http://winrun4j.sourceforge.net
        - http://jsmooth.sourceforge.net