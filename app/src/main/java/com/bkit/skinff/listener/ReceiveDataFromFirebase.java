package com.bkit.skinff.listener;

import com.bkit.skinff.model.FileData;

import java.util.ArrayList;
import java.util.List;

public interface ReceiveDataFromFirebase {
    void data(ArrayList<FileData> list);
}
