package com.example.miwokapp;

public class Word {
    private String mmiwokTranslate;
    private String mdefaultTranslate;
    private int mImageResourseId=NO_IMAGE_SHOWN;
    private static final int NO_IMAGE_SHOWN=-1;
    private int mAudioResourseId;

    public Word(String miwokTranslate, String defaultTranslate,int AudioResourseId)
    {
        mmiwokTranslate=miwokTranslate;
        mdefaultTranslate=defaultTranslate;
        mAudioResourseId=AudioResourseId;
    }

    public Word(String miwokTranslate, String defaultTranslate,int ImageResourseId,int AudioResourseId)
    {
        mmiwokTranslate=miwokTranslate;
        mdefaultTranslate=defaultTranslate;
        mImageResourseId=ImageResourseId;
        mAudioResourseId=AudioResourseId;
    }
    public String getMmiwokTranslate()
    {
        return mmiwokTranslate;
    }
    public String getMdefaultTranslate()
    {
        return mdefaultTranslate;
    }
    public int getmImageResourseId()
    {
        return mImageResourseId;
    }
    public boolean hasImage(){
        return mImageResourseId != NO_IMAGE_SHOWN;
    }

    public int getmAudioResourseId() {
        return mAudioResourseId;
    }
}
