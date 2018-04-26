# HadoopApp

create .jar with "mvn package" command on project directory.

upload .jar to Amazon s3 bucket.

create amazon EMR cluster

Submit job via web interface => choose jar file, set argumnets for Word Count arguments: WordCout inputFile OutputFile
or connet masterNode with ssh and command "hadoop jarname.jar MainClassName.java argumnets" 
