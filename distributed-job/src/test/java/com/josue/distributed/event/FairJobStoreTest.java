//package com.josue.distributed.event;
//
//import com.josue.distributed.JobEvent;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
///**
// * Created by Josue on 26/07/2016.
// */
//public class FairJobStoreTest {
//
//    private FairJobStore store = new FairJobStore();
//
//    @Before
//    public void init() {
//        store.jobs = new ConcurrentLinkedQueue<>();
//        store.backup = new HashMap<>();
//    }
//
//    @Test
//    public void testAddNull() throws Exception {
//        store.add(null);
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testAddEmpty() throws Exception {
//        store.add(Arrays.asList());
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testAddNullMasterId() throws Exception {
//        store.add(Arrays.asList(new JobEvent(null)));
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testAddNEmptyMasterId() throws Exception {
//        store.add(Arrays.asList(new JobEvent("")));
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testGet() throws Exception {
//        JobEvent jobEvent1 = new JobEvent("master1");
//        JobEvent jobEvent2 = new JobEvent("master2");
//
//        store.add(Arrays.asList(jobEvent1, jobEvent2));
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//        foundEvent = store.get();
//        assertEquals(jobEvent2, foundEvent);
//    }
//
//    @Test
//    public void testGetTwoItems() throws Exception {
//        JobEvent jobA = new JobEvent("master1");
//        JobEvent jobB = new JobEvent("master1");
//        store.add(Arrays.asList(jobA, jobB));
//
//        JobEvent jobC = new JobEvent("master2");
//        JobEvent jobD = new JobEvent("master2");
//        store.add(Arrays.asList(jobC, jobD));
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobA, foundEvent);
//
//        foundEvent = store.get();
//        assertEquals(jobC, foundEvent);
//
//        foundEvent = store.get();
//        assertEquals(jobB, foundEvent);
//
//        foundEvent = store.get();
//        assertEquals(jobD, foundEvent);
//    }
//
//    @Test
//    public void testGetBackup() throws Exception {
//        JobEvent jobEvent1 = new JobEvent("master1");
//
//        store.add(Arrays.asList(jobEvent1));
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//        JobEvent released = store.releaseJob(jobEvent1.getId());
//        assertEquals(jobEvent1, released);
//    }
//
//    @Test
//    public void testGetNull() throws Exception {
//        assertNull(store.get());
//
//        JobEvent jobEvent1 = new JobEvent("master1");
//        store.add(Arrays.asList(jobEvent1));
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testGetIteratorReset() throws Exception {
//        JobEvent jobEvent1 = new JobEvent("master1");
//        store.add(Arrays.asList(jobEvent1));
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//        store.add(Arrays.asList(jobEvent1));
//
//        foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//    }
//
//}