package com.josue.batch.agent.core;

/**
 * Created by Josue on 04/08/2016.
 */
public class MeterHint {

    public static final String TOTAL = "total";

    public static final String LISTENERINIT = "listenerInit";
    public static final String ONSTART = "onStart";
    public static final String ONERROR = "onError";
    public static final String ONSUCCESS = "onSuccess";

    public static final String READERINIT = "readerInit";
    public static final String READ = "read";
    public static final String READCLOSE = "readClose";

    public static final String PROCESSORINIT = "processorInit";
    public static final String PROCCESS = "proccess";
    public static final String PROCCESSORCLOSE = "processorClose";

    public static final String WRITERINIT = "writerInit";
    public static final String WRITE = "write";
    public static final String WRITECLOSE = "writeClose";

    public static final String CHUNKLETINIT = "chunkletInit";
    public static final String CHUNKLETEXECUTE = "chunkletExecute";
    public static final String CHUNKLETCLOSE = "chunkletClose";


}
