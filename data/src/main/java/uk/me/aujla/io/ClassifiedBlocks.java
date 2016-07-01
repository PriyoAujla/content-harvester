package uk.me.aujla.io;

import uk.me.aujla.model.ClassifiedTextBlock;

import java.util.Collection;
import java.util.List;

public class ClassifiedBlocks {

    private String id;
    private List<ClassifiedTextBlock> classifiedBlocks;

    public ClassifiedBlocks(String id, List<ClassifiedTextBlock> classifiedBlocks) {
        this.id = id;
        this.classifiedBlocks = classifiedBlocks;
    }

    public String getId() {
        return id;
    }

    public List<ClassifiedTextBlock> getClassifiedBlocks() {
        return classifiedBlocks;
    }
}
