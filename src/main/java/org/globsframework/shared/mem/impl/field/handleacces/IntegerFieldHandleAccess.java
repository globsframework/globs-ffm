package org.globsframework.shared.mem.impl.field.handleacces;

import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.fields.LongField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.shared.mem.impl.read.ReadContext;
import org.globsframework.shared.mem.impl.write.SaveContext;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

public class IntegerFieldHandleAccess implements HandleAccess {
    private final VarHandle varHandle;
    private final IntegerField field;

    public IntegerFieldHandleAccess(VarHandle varHandle, IntegerField field) {
        this.varHandle = varHandle;
        this.field = field;
    }

    public static HandleAccess create(GroupLayout groupLayout, IntegerField field) {
        return new IntegerFieldHandleAccess(groupLayout.varHandle(MemoryLayout.PathElement.groupElement(field.getName())), field);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public void save(Glob data, MemorySegment memorySegment, long offset, SaveContext saveContext) {
        final int value = data.get(field, 0);
        varHandle.set(memorySegment, offset, value);
    }

    @Override
    public void readAtOffset(MutableGlob data, MemorySegment memorySegment, long offset, ReadContext readContext) {
        data.set(field, (int)varHandle.get(memorySegment, offset));
    }

}
