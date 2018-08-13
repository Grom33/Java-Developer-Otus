

Options VM:
-Xms256m -Xmx256m -XX:MaxMetaspaceSize=128m
-XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=./dumps/
-XX:+UseConcMarkSweepGC





**-XX:+UseConcMarkSweepGC**
    1:pass
    Collections name:Young Gen GC; Count of collections: 9;  Avg count: 0.22222222 per min.; Avg duration: 0.018777778 s. per collection Total time STW: 0.169 sec.
    Collections name:Old Gen GC; Count of collections: 46;  Avg count: 0.04347826 per min.; Avg duration: 0.4434783 s. per collection Total time STW: 20.4 sec.
    Count of operations: 4498312
    2:pass
    Collections name:Young Gen GC; Count of collections: 8;  Avg count: 0.25 per min.; Avg duration: 0.025375 s. per collection Total time STW: 0.203 sec.
    Collections name:Old Gen GC; Count of collections: 49;  Avg count: 0.040816326 per min.; Avg duration: 0.8480612 s. per collection Total time STW: 41.555 sec.
    Count of operations: 4533928
    3:pass
    Collections name:Young Gen GC; Count of collections: 8;  Avg count: 0.25 per min.; Avg duration: 0.022125 s. per collection Total time STW: 0.177 sec.
    Collections name:Old Gen GC; Count of collections: 49;  Avg count: 0.040816326 per min.; Avg duration: 0.8327959 s. per collection Total time STW: 40.807 sec.
    Count of operations: 4533847

-XX:+UseG1GC
    1:pass
    Collections name:Young Gen GC; Count of collections: 29;  Avg count: 0.06896552 per min.; Avg duration: 0.010655172 s. per collection Total time STW: 0.309 sec.
    Collections name:Old Gen GC; Count of collections: 6;  Avg count: 0.33333334 per min.; Avg duration: 0.12433334 s. per collection Total time STW: 0.746 sec.
    Count of operations: 4648393
    2:pass
    Collections name:Young Gen GC; Count of collections: 24;  Avg count: 0.083333336 per min.; Avg duration: 0.012583333 s. per collection Total time STW: 0.302 sec.
    Collections name:Old Gen GC; Count of collections: 4;  Avg count: 0.5 per min.; Avg duration: 0.1145 s. per collection Total time STW: 0.458 sec.
    Count of operations: 4675349
    3:pass
    Collections name:Young Gen GC; Count of collections: 33;  Avg count: 0.09090909 per min.; Avg duration: 0.008757576 s. per collection Total time STW: 0.289 sec.
    Collections name:Old Gen GC; Count of collections: 7;  Avg count: 0.42857143 per min.; Avg duration: 0.11628571 s. per collection Total time STW: 0.814 sec.
    Count of operations: 4684995
    

-XX:+UseSerialGC
    1: pass 
    Collections name:Young Gen GC; Count of collections: 4;  Avg count: 0.5 per min.; Avg duration: 0.05125 s. per collection Total time STW: 0.205 sec.
    Collections name:Old Gen GC; Count of collections: 13;  Avg count: 0.15384616 per min.; Avg duration: 0.17853847 s. per collection Total time STW: 2.321 sec.
    Count of operations: 4507988
    2: pass
    Collections name:Young Gen GC; Count of collections: 4;  Avg count: 0.5 per min.; Avg duration: 0.03675 s. per collection Total time STW: 0.147 sec.
    Collections name:Old Gen GC; Count of collections: 46;  Avg count: 0.06521739 per min.; Avg duration: 0.16791303 s. per collection Total time STW: 7.724 sec.
    Count of operations: 4534129
    3:pass
    Collections name:Young Gen GC; Count of collections: 4;  Avg count: 0.5 per min.; Avg duration: 0.03325 s. per collection Total time STW: 0.133 sec.
    Collections name:Old Gen GC; Count of collections: 44;  Avg count: 0.06818182 per min.; Avg duration: 0.17077272 s. per collection Total time STW: 7.514 sec.
    Count of operations: 4534132