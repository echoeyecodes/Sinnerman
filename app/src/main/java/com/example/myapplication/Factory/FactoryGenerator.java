package com.example.myapplication.Factory;

import com.example.myapplication.Factory.Factories.HomeVideoAdapterFactory;
import com.example.myapplication.Factory.Factories.MiscAdapterFactory;
import com.example.myapplication.Factory.Factories.RecentAdapterFactory;

public class FactoryGenerator{

    public static CustomAdapters getFactory(TYPE type){
        switch (type){
            case VIDEOS:
                return new HomeVideoAdapterFactory();
            case RECENTS:
                return new RecentAdapterFactory();
            case MISC:
                return new MiscAdapterFactory();
            default:
                return null;
        }

    }
}
