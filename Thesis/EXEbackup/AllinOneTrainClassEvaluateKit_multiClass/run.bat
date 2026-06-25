svm_multiclass_learn -c 32768 "3. SVMTrainFile.txt" "model" 
svm_multiclass_classify "3. SVMTestFile.txt" "model" "predictions"
java -jar -Xmx20g Evaluation.jar
pause