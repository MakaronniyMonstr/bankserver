package com.vesko.configuration;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.net.URL;
import java.util.Collections;
import java.util.List;

@Data
public class Properties implements Validatable {
    private URL host;
    private Integer threadCount;
    private Integer readQuota;
    private Integer writeQuota;
    private List<Integer> readIdList;
    private List<Integer> writeIdList;

    @JsonSetter
    public void setReadIdList(List<Integer> readIdList) {
        this.readIdList = Collections.unmodifiableList(readIdList);
    }

    @JsonSetter
    public void setWriteIdList(List<Integer> writeIdList) {
        this.writeIdList = Collections.unmodifiableList(writeIdList);
    }

    @Override
    public void validate() {
        if (getThreadCount() < 0)
            throw new IllegalArgumentException("Invalid thread count");
        if (getReadQuota() < 0)
            throw new IllegalArgumentException("readQuota must be >= 0");
        if (getWriteQuota() < 0)
            throw new IllegalArgumentException("writeQuota must be >= 0");
        if (getWriteIdList().isEmpty())
            throw new IllegalArgumentException("writeIdList must not be empty");
        if (getReadIdList().isEmpty())
            throw new IllegalArgumentException("readIdList must not be empty");
        if (getReadQuota() + getWriteQuota() == 0)
            throw new IllegalArgumentException("readQuota + writeQuota must be > 0");
    }
}
