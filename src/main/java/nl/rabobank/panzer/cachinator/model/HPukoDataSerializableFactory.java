package nl.rabobank.panzer.cachinator.model;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class HPukoDataSerializableFactory implements DataSerializableFactory {
    static final int ID = 1;
    static final int ClassID= 1;
    @Override
    public IdentifiedDataSerializable create(int classid) {
        if (classid == ClassID) {
            return null; //new HazelcastPuko(); //just to make it compile
        } else {
            return null;
        }
    }
}
