package com.bkit.skinff.listener;

import com.bkit.skinff.model.FileData;

public interface ClickToModify {
    void modifyData(FileData fileData);
    void updateData(FileData fileData);
    void getPos(int position);

}
