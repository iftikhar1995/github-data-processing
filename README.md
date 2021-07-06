# GitHub Data Processing
The project includes the analysis performed on github data.

## Built With
- Scala
- Apache Spark
- Apache Maven

## Prerequisites
[Installation of Maven](https://maven.apache.org/install.html)

## Execution
1. Clone the repo <br> `git clone https://github.com/iftikhar1995/github-data-processing.git`
2. Install the maven dependencies. Open a terminal on the root of the folder and run following command:
<br>`mvn clean install`
3. Go to `src/main/scala/com/iftikhar/processing/Driver.scala` file and run it.

## Resources
The data files associated with the analysis are present at `src/main/resources`. If you want to provide you own
data files then kindly change the paths of respective data file in
`src/main/scala/com/iftikhar/processing/utils/Config.scala` file. Apart from the path you can also change the 
spark application name and the spark master from the file as well.
 