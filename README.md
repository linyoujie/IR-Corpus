# IR Project 
 
Part one of the IR project: SearchEngine 
Author: Youjie Lin

SearchEngine.java is the main function of project file, use the CommandLine to run the program such as: to compiling file:
    javac SearchEngine.java
To run the program with CommandLine flags such as:
    java SearchEngine -CorpusDir PathOfDir -InvertedIndex NameOfIIndexFile -StopList NameOfStopListFile -Queries QueryFile -Results ResultsFile -inversedIn trueOrFalse

 PathOfDir is the Corpus files should be; and with "-inversedIn true", the InvertedIndex will be outputted, NameOfIIndexFile will be the output text file name; NameOfStopListFile is the file that contain your stopwords, each word will take a newline; QueryFile contains the queries and outputted results will output to ResultsFile.

for any unspecify flag will set to defaut value as follow:
        String CorpusDir ="./RawHtml";
        String InvertedIndex="./InvertedIndex.txt";
        String StopList="./StopList.txt";
        String Queries="./query.txt"; 
        String Results="./result.txt";
        String inversedIn = "false";

You must have a valid Folder and StopList to start the program, any invalid file of Queries and StopList will terminated the program.

