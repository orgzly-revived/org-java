package com.orgzly.org;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OrgFileSettingsTest {

    @Test
    public void testGetFiletagsListWithSingleTag() {
        String preface = "#+FILETAGS: :tag1:";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertEquals(1, tags.size());
        Assert.assertEquals("tag1", tags.get(0));
    }

    @Test
    public void testGetFiletagsListWithMultipleTags() {
        String preface = "#+FILETAGS: :tag1:tag2:tag3:";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertEquals(3, tags.size());
        Assert.assertEquals("tag1", tags.get(0));
        Assert.assertEquals("tag2", tags.get(1));
        Assert.assertEquals("tag3", tags.get(2));
    }

    @Test
    public void testGetFiletagsListWithSpaces() {
        String preface = "#+FILETAGS: : tag1 : tag2 :";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertEquals(2, tags.size());
        Assert.assertEquals("tag1", tags.get(0));
        Assert.assertEquals("tag2", tags.get(1));
    }

    @Test
    public void testGetFiletagsListWithMultipleFiletagsLines() {
        String preface = "#+FILETAGS: :tag1:tag2:\n#+FILETAGS: :tag3:";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertEquals(3, tags.size());
        Assert.assertEquals("tag1", tags.get(0));
        Assert.assertEquals("tag2", tags.get(1));
        Assert.assertEquals("tag3", tags.get(2));
    }

    @Test
    public void testGetFiletagsListWithNoFiletags() {
        String preface = "#+TITLE: My Document";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertTrue(tags.isEmpty());
    }

    @Test
    public void testGetFiletagsListWithEmptyValue() {
        String preface = "#+FILETAGS: ::";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertTrue(tags.isEmpty());
    }

    @Test
    public void testGetFiletagsListWithMixedContent() {
        String preface = "#+TITLE: Test\n#+FILETAGS: :work:project:\n#+AUTHOR: Test Author";
        OrgFileSettings settings = OrgFileSettings.fromPreface(preface);

        List<String> tags = settings.getFiletags();

        Assert.assertEquals(2, tags.size());
        Assert.assertEquals("work", tags.get(0));
        Assert.assertEquals("project", tags.get(1));
        Assert.assertEquals("Test", settings.getTitle());
    }
}
