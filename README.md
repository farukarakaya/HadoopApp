

![MapReduceAndAws](http://tw.greywool.com/i/nYuyk.png)
 
## Finding Similar Images to an Input Image in a Large Dataset with MapReduce

This project aims to use MapReduce for finding image similarity in large dataset. 

## White Paper
You can find paper in Paper folder or just simply following [this link](https://github.com/serhangursoy/MapReduceImageSimilarity/blob/SIFTdev/Paper/White_Paper.pdf). You can read our approaches and results in the white paper.

### Prerequisites
You need Java for compiling the JAR.
To run this code properly, you need your own AWS account with S3 and EMR access. 
You also require decent internet connection for checking the status of the clusters.

### Running
To use this project, you have to do following, 
```
- Clone this project
- Make sure you have Maven Installed. If you are using Intellij it should come in default package
- Create JAR with Maven with create .jar with "mvn package" command on project directory.
- Create your cluster from AWS EMR Control Panel
- Once your cluters are up and running, go to your S3 bucket and upload your JAR.
- Open up AWS EMR Control Panel again, add a new step, select freshly installed JAR and add the following arguments. 
Ex GistCompare s3://com-rosettahub-default-xxxxx/MapReduce/input/ s3://com-rosettahub-default-xxxxx/output 20000 com-rosettahub-default-xxxxx
- Once everything is done, there should be final file in MapReduce folder. You can review similarities 
```

## Built With

* [MapReduce](https://hadoop.apache.org/docs/r1.2.1/mapred_tutorial.html) - Main framework
* [Hadoop](http://hadoop.apache.org/) - Used for HDFS and general MapReduce Framework
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Serhan Gürsoy** - *Architecture Engineer* - [Github](https://github.com/serhangursoy)
* **Ege Yosunkaya** - *Architecture Engineer* - [Github](https://github.com/egeyosunkaya)
* **Ömer Faruk Karakaya** - *Architecture Engineer* - [Github](https://github.com/farukarakaya/)
* **Musab Erayman** - *Architecture Engineer* - [Github](https://github.com/merayman)

See also the list of [contributors](https://github.com/serhangursoy/MapReduceImageSimilarity/contributors) who participated in this project.
