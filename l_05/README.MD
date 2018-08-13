Было произведено по 3 запуска программы с каждым из GC.
В качестве основных метрик были выбраны количество произведенных операций вставки значений в List (Count of operations)
и продолжительность Stop The World (Total time STW).   
Для данного типа приложения наиболее производительным показал себя GC G1 с количеством произведенных операций 4675349 и суммарным STW 0.76 сек., на втором месте SerialGC с 4534129 операциями и общим STW 8.484 сек., на последнем месте  ConcMarkSweepGC, хотя количество операций почти не отличается от SerialGC 4533928 зато время STW составляет 41.758 сек..

**Options VM:**
-Xms256m -Xmx256m -XX:MaxMetaspaceSize=128m
-XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=./dumps/

**Результаты:**
**Concurrent Mark Sweep  -XX:+UseConcMarkSweepGC**
   
   *Collections name:Young Gen GC;*
    Count of collections: 8;  
    Avg count: 0.25 per min.; 
    Avg duration: 0.025375 s. per collection 
    Total time STW: 0.203 sec.
    
    *Collections name:Old Gen GC;* 
    Count of collections: 49;  
    Avg count: 0.040816326 per min.; 
    Avg duration: 0.8480612 s. per collection 
    Total time STW: 41.555 sec.
    
    **Total time STW: 41.758 sec.**
    **Count of operations: 4533928**
   

**-XX:+UseG1GC**
    Collections name:Young Gen GC; 
    Count of collections: 24;  
    Avg count: 0.083333336 per min.; 
    Avg duration: 0.012583333 s. per collection 
    Total time STW: 0.302 sec.
    
    Collections name:Old Gen GC; 
    Count of collections: 4;  
    Avg count: 0.5 per min.; 
    Avg duration: 0.1145 s. per collection 
    Total time STW: 0.458 sec.
    
    Total time STW: 0.76 sec.
    Count of operations: 4675349
   

**-XX:+UseSerialGC**
    Collections name:Young Gen GC; 
    Count of collections: 4;  
    Avg count: 0.5 per min.; 
    Avg duration: 0.03675 s. per collection 
    Total time STW: 0.147 sec.
    
    Collections name:Old Gen GC; 
    Count of collections: 46;  
    Avg count: 0.06521739 per min.; 
    Avg duration: 0.16791303 s. per collection 
    Total time STW: 7.724 sec.
    
    Total time STW: 8.484 sec.
    Count of operations: 4534129
    
