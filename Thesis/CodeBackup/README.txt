============================================================
=                 CrossValidationSampling                  =
============================================================
How it work: it split a vector file into about 10 folds.
Each fold contains:
- PicID list: list of all picture ID in TEST set.
- 3. SVMTestFile.txt: the fold which is used as test set
- 3. SVMTrainFile.txt: the rest 9 folds which is used as tran set
- sed_task2_gs: groundtruth of TEST set
Input:
- 3. SVMTrainFile: the FULL file after feature extraction for training set at ME2013
- FullTextTrain_Output_Raw: the raw data, inculded PicID and its text
- sed2013_task2_dataset_train_gs.csv: the FULL version of groundtruth provied by ME2013 task organizers

