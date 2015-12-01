/*
 * Created on 03/11/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import junit.framework.Assert;

import org.junit.Test;

import database.metadata.Index;

/**
 * Testes da classe Index
 * 
 * @author ricardo.reiter
 */
public class IndexTest {

    @Test
    public void testGet_Equals_001() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesEquals(new Integer(50)).toArray();
        Assert.assertEquals(4, list.length);
        Assert.assertEquals(1, list[0]);
        Assert.assertEquals(5, list[1]);
        Assert.assertEquals(4, list[2]);
        Assert.assertEquals(16, list[3]);

        list = index.getIndexesEquals(new Integer(51)).toArray();
        Assert.assertEquals(2, list.length);
        Assert.assertEquals(2, list[0]);
        Assert.assertEquals(3, list[1]);

        list = index.getIndexesEquals(new Integer(10)).toArray();
        Assert.assertEquals(3, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
    }

    @Test
    public void testGet_Equals_002() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesEquals(new Integer(12)).toArray();
        Assert.assertEquals(0, list.length);
    }

    @Test
    public void testGet_NotEquals_001() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesNotEquals(new Integer(50)).toArray();
        Assert.assertEquals(7, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(2, list[5]);
        Assert.assertEquals(3, list[6]);
    }

    @Test
    public void testGet_NotEquals_002() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesNotEquals(new Integer(25)).toArray();
        Assert.assertEquals(11, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
        Assert.assertEquals(2, list[9]);
        Assert.assertEquals(3, list[10]);
    }

    @Test
    public void testGet_Greater_001() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesGreater(new Integer(50)).toArray();
        Assert.assertEquals(2, list.length);
        Assert.assertEquals(2, list[0]);
        Assert.assertEquals(3, list[1]);

        list = index.getIndexesGreater(new Integer(45)).toArray();
        Assert.assertEquals(7, list.length);
        Assert.assertEquals(6, list[0]);
        Assert.assertEquals(1, list[1]);
        Assert.assertEquals(5, list[2]);
        Assert.assertEquals(4, list[3]);
        Assert.assertEquals(16, list[4]);
        Assert.assertEquals(2, list[5]);
        Assert.assertEquals(3, list[6]);

        list = index.getIndexesGreater(new Integer(46)).toArray();
        Assert.assertEquals(7, list.length);
        Assert.assertEquals(6, list[0]);
        Assert.assertEquals(1, list[1]);
        Assert.assertEquals(5, list[2]);
        Assert.assertEquals(4, list[3]);
        Assert.assertEquals(16, list[4]);
        Assert.assertEquals(2, list[5]);
        Assert.assertEquals(3, list[6]);

        list = index.getIndexesGreater(new Integer(9)).toArray();
        Assert.assertEquals(11, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
        Assert.assertEquals(2, list[9]);
        Assert.assertEquals(3, list[10]);
    }

    @Test
    public void testGet_Greater_002() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesGreater(new Integer(51)).toArray();
        Assert.assertEquals(0, list.length);

        list = index.getIndexesGreater(new Integer(52)).toArray();
        Assert.assertEquals(0, list.length);
    }

    @Test
    public void testGet_GreaterOrEquals_001() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesGreaterOrEquals(new Integer(50)).toArray();
        Assert.assertEquals(6, list.length);
        Assert.assertEquals(1, list[0]);
        Assert.assertEquals(5, list[1]);
        Assert.assertEquals(4, list[2]);
        Assert.assertEquals(16, list[3]);
        Assert.assertEquals(2, list[4]);
        Assert.assertEquals(3, list[5]);

        list = index.getIndexesGreaterOrEquals(new Integer(45)).toArray();
        Assert.assertEquals(8, list.length);
        Assert.assertEquals(7, list[0]);
        Assert.assertEquals(6, list[1]);
        Assert.assertEquals(1, list[2]);
        Assert.assertEquals(5, list[3]);
        Assert.assertEquals(4, list[4]);
        Assert.assertEquals(16, list[5]);
        Assert.assertEquals(2, list[6]);
        Assert.assertEquals(3, list[7]);

        list = index.getIndexesGreaterOrEquals(new Integer(46)).toArray();
        Assert.assertEquals(7, list.length);
        Assert.assertEquals(6, list[0]);
        Assert.assertEquals(1, list[1]);
        Assert.assertEquals(5, list[2]);
        Assert.assertEquals(4, list[3]);
        Assert.assertEquals(16, list[4]);
        Assert.assertEquals(2, list[5]);
        Assert.assertEquals(3, list[6]);

        list = index.getIndexesGreaterOrEquals(new Integer(9)).toArray();
        Assert.assertEquals(11, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
        Assert.assertEquals(2, list[9]);
        Assert.assertEquals(3, list[10]);
    }

    @Test
    public void testGet_GreaterOrEquals_002() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesGreaterOrEquals(new Integer(51)).toArray();
        Assert.assertEquals(2, list.length);

        list = index.getIndexesGreaterOrEquals(new Integer(52)).toArray();
        Assert.assertEquals(0, list.length);
    }

    @Test
    public void testGet_Less_001() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesLess(new Integer(45)).toArray();
        Assert.assertEquals(3, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);

        list = index.getIndexesLess(new Integer(12)).toArray();
        Assert.assertEquals(3, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);

        list = index.getIndexesLess(new Integer(46)).toArray();
        Assert.assertEquals(4, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);

        list = index.getIndexesLess(new Integer(52)).toArray();
        Assert.assertEquals(11, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
        Assert.assertEquals(2, list[9]);
        Assert.assertEquals(3, list[10]);

        list = index.getIndexesLess(new Integer(51)).toArray();
        Assert.assertEquals(9, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
    }

    @Test
    public void testGet_Less_002() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesLess(new Integer(10)).toArray();
        Assert.assertEquals(0, list.length);

        list = index.getIndexesLess(new Integer(8)).toArray();
        Assert.assertEquals(0, list.length);
    }

    @Test
    public void testGet_LessOrEquals_001() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesLessOrEquals(new Integer(45)).toArray();
        Assert.assertEquals(4, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);

        list = index.getIndexesLessOrEquals(new Integer(12)).toArray();
        Assert.assertEquals(3, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);

        list = index.getIndexesLessOrEquals(new Integer(46)).toArray();
        Assert.assertEquals(4, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);

        list = index.getIndexesLessOrEquals(new Integer(52)).toArray();
        Assert.assertEquals(11, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
        Assert.assertEquals(2, list[9]);
        Assert.assertEquals(3, list[10]);

        list = index.getIndexesLessOrEquals(new Integer(51)).toArray();
        Assert.assertEquals(11, list.length);
        Assert.assertEquals(15, list[0]);
        Assert.assertEquals(14, list[1]);
        Assert.assertEquals(13, list[2]);
        Assert.assertEquals(7, list[3]);
        Assert.assertEquals(6, list[4]);
        Assert.assertEquals(1, list[5]);
        Assert.assertEquals(5, list[6]);
        Assert.assertEquals(4, list[7]);
        Assert.assertEquals(16, list[8]);
        Assert.assertEquals(2, list[9]);
        Assert.assertEquals(3, list[10]);
    }

    @Test
    public void testGet_LessOrEquals_002() {
        Index index = createIntIndex();
        Object[] list = index.getIndexesLessOrEquals(new Integer(10)).toArray();
        Assert.assertEquals(3, list.length);

        list = index.getIndexesLessOrEquals(new Integer(8)).toArray();
        Assert.assertEquals(0, list.length);
    }

    //    @Test
    //    public void testPerformance001() {
    //        Index index = new Index();
    //        for (int i = 0; i < 40000; i++) {
    //            index.put(i, i);
    //        }
    //
    //        long initialTime = System.currentTimeMillis();
    //        index.getIndexesGreater(19000);
    //        index.getIndexesLessOrEquals(19000);
    //        System.out.println("Levou " + (System.currentTimeMillis() - initialTime));
    //    }

    private Index createIntIndex() {
        Index index = new Index(5);
        index.put(50, 1);
        index.put(50, 5);
        index.put(50, 4);

        index.put(51, 2);
        index.put(51, 3);

        index.put(49, 6);

        index.put(45, 7);

        index.put(10, 15);
        index.put(10, 14);
        index.put(10, 13);

        index.put(50, 16);
        return index;
    }

}
