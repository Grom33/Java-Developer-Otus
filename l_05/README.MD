**Metrics**

В качестве метрик были выбраны:
 - количества произведенных операций вставки значений в List в секунду;
 - среднее значение интервала времени между каждой 100_000ой вставкой;
 - Суммарное количество произведенных операций вставки до ООМ;
    
**Environment**

    java 10.0.2 2018-07-17
    Java(TM) SE Runtime Environment 18.3 (build 10.0.2+13)
    Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10.0.2+13, mixed mode)

**Options VM:**

    -Xms256m -Xmx256m -XX:MaxMetaspaceSize=128m

**Результаты тестирования для каждых пар GC:**

-XX:+UseG1GC
    
    Duration of 100_000 operations : 7.186 sec; 
    Operations per second: 13915; 
    Count of operations : 4686227; 
    Time of life application: 337 sec.;
    
    Generation type:Young; 
    GC name: G1 Young Generation; 
    Count of collections: 29; 
    Average count: 5.8 per min.; 
    Average duration: 0.020137932 s. per collection; 
    Total Duration: 0.584 sec.; 
    ------------------------
    Generation type:Old; 
    GC name: G1 Old Generation; 
    Count of collections: 7; 
    Average count: 1.4 per min.; 
    Average duration: 0.15357143 s. per collection; 
    Total Duration: 1.075 sec.; 
    
-XX:+UseParallelGC
    
    Duration of 100_000 operations : 7.202 sec; 
    Operations per second: 13885; 
    Count of operations : 4225226; 
    Time of life application: 313 sec.;
    
    Generation type:Young; 
    GC name: PS Scavenge; 
    Count of collections: 3; 
    Average count: 0.6 per min.; 
    Average duration: 0.06733333 s. per collection; 
    Total Duration: 0.202 sec.; 
    ------------------------
    Generation type:Old; 
    GC name: PS MarkSweep; 
    Count of collections: 36; 
    Average count: 7.2 per min.; 
    Average duration: 0.21277776 s. per collection; 
    Total Duration: 7.66 sec.;

-XX:+UseSerialGC
    
    Duration of 100_000 operations : 7.229 sec; 
    Operations per second: 13833; 
    Count of operations : 4533738; 
    Time of life application: 342 sec.;
    
    Generation type:Young; 
    GC name: Copy; 
    Count of collections: 4; 
    Average count: 0.8 per min.; 
    Average duration: 0.0515 s. per collection; 
    Total Duration: 0.206 sec.; 
    ------------------------
    Generation type:Old; 
    GC name: MarkSweepCompact; 
    Count of collections: 40; 
    Average count: 8.0 per min.; 
    Average duration: 0.1854 s. per collection; 
    Total Duration: 7.416 sec.; 
         
-XX:+UseConcMarkSweepGC
     
    Duration of 100_000 operations : 7.33 sec; 
    Operations per second: 13642; 
    Count of operations : 4533277; 
    Time of life application: 339 sec.;
    
    Generation type:Young; 
    GC name: ParNew; 
    Count of collections: 15; 
    Average count: 3.0 per min.; 
    Average duration: 0.0264 s. per collection; 
    Total Duration: 0.396 sec.; 
    ------------------------
    Generation type:Old; 
    GC name: ConcurrentMarkSweep; 
    Count of collections: 89; 
    Average count: 17.8 per min.; 
    Average duration: 0.92664045 s. per collection; 
    Total Duration: 82.471 sec.; 

**Выводы**

Для данного типа приложения (пактеная обработка) наиболее производительным показал себя GC G1 с количеством произведенных 13915 операций в секунду, так же общее количество вставок до ООМ лидирует что говорит о более эффективной работе с памятью;
На втором месте PS Scavenge/PS MarkSweep, производительность так же высока, но вот эффективность работы с памятью оставляет желать лучшего, количество вставок до ООМ самое низкое.
На третьем месте Copy / MarkSweepCompact;
На четвертом ParNew/ConcurrentMarkSweep 


   

    

    