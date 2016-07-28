//package com.josue.distributed.event;
//
//import com.josue.distributed.JobEvent;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.HashMap;
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
//        store.jobs = new HashMap<>();
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
//    public void testAddNullMasterId() throws Exception {
//        store.add(new JobEvent(null, 0, 0));
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testAddNEmptyMasterId() throws Exception {
//        store.add(new JobEvent("", 0, 0));
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testGet() throws Exception {
//        JobEvent jobEvent1 = new JobEvent("master1", 0, 0);
//        JobEvent jobEvent2 = new JobEvent("master2", 0, 0);
//
//        store.add(jobEvent1);
//        store.add(jobEvent2);
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
//        JobEvent jobA = new JobEvent("master1", 0, 0);
//        JobEvent jobB = new JobEvent("master1", 0, 0);
//        store.add(jobA);
//        store.add(jobB);
//
//        JobEvent jobC = new JobEvent("master2", 0, 0);
//        JobEvent jobD = new JobEvent("master2", 0, 0);
//        store.add(jobC);
//        store.add(jobD);
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
//        JobEvent jobEvent1 = new JobEvent("master1", 0, 0);
//
//        store.add(jobEvent1);
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
//        JobEvent jobEvent1 = new JobEvent("master1", 0, 0);
//        store.add(jobEvent1);
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//        assertNull(store.get());
//    }
//
//    @Test
//    public void testGetIteratorReset() throws Exception {
//        JobEvent jobEvent1 = new JobEvent("master1", 0, 0);
//        store.add(jobEvent1);
//
//        JobEvent foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//        store.add(jobEvent1);
//
//        foundEvent = store.get();
//        assertEquals(jobEvent1, foundEvent);
//
//    }
//
//}