package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.wmy.exceptions.ExecutionException;

public interface IBarrierTable extends ICloneable {
    public class BarrierTableEntry implements ICloneable {
        private Integer count;
        private IList<Integer> waitList;

        public BarrierTableEntry(Integer count, IList<Integer> waitList) {
            this.count = count;
            this.waitList = waitList;
        }

        public Integer getCount() {
            return count;
        }

        public IList<Integer> getWaitList() {
            return waitList;
        }

        @Override
        public Object deepCopy() {
            return new BarrierTableEntry(count, waitList.deepCopy());
        }

        @Override
        public String toString() {
            return "(" + count + ", " + waitList.stream().map(e -> e.toString()).collect(Collectors.joining(", "))
                    + ")";
        }
    }

    BarrierTableEntry at(Integer key) throws ExecutionException;

    void set(Integer key, BarrierTableEntry value);

    Integer add(Integer count, IList<Integer> waitList);

    void del(Integer key);

    void setContent(Map<Integer, BarrierTableEntry> content);

    boolean contains(Integer key);

    Set<Entry<Integer, BarrierTableEntry>> entrySet();
}
