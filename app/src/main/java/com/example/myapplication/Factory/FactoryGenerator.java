package com.example.myapplication.Factory;

import com.example.myapplication.Factory.Factories.HomeVideoAdapterFactory;

public class FactoryGenerator{

    public static CustomAdapters getFactory(TYPE type){
        switch (type){
            case VIDEOS:
                return new HomeVideoAdapterFactory();
            default:
                return null;
        }

    }
}
