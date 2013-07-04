package com.speedledger.translate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a java-property-file. Lines keep their order.
 */
public class JavaPropertyFile {
    private List<JavaProperty> content=new ArrayList<JavaProperty>();

    public List<JavaProperty> getContent() {
        return content;
    }

    public void setContent(List<JavaProperty> content) {
        this.content = content;
    }

    public int findItem(String key) {
        for (int i = 0; i < content.size(); i++) {
            JavaProperty prop = content.get(i);
            if(prop instanceof TranslatableItem) {
                TranslatableItem item= (TranslatableItem) prop;
                if(item.getKey().equals(key)) return i;
            }
        }
        return -1;
    }

}
