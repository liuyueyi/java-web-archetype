package com.mushroom.hui.common.invoke;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yihui on 16/4/10.
 */
public class Params implements Serializable {
    private static final long serialVersionUID = -6812785035412209228L;

    private String target;
    private String cls;
    private String method;
    private Map<String, String> params;

    public Params() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        if (CollectionUtils.isEmpty(params)) {
            return Collections.emptyMap();
        }
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void addParam(String key, String value) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key is empty!");
        }

        if (CollectionUtils.isEmpty(params)) {
            params = new LinkedHashMap<>();
        }

        params.put(key, value);
    }

    public boolean isValiad() {
        return !(StringUtils.isBlank(cls) || StringUtils.isBlank(method));
    }

    @Override
    public String toString() {
        return "Params{" +
                "target='" + target + '\'' +
                ", cls='" + cls + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
