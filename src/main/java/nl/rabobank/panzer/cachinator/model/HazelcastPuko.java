/*
 * RedisPuko.java, 11 Jul 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 11 Jul 2018
 *
 */
@Getter
@Setter
@KeySpace("nl.rabobank.replicated.HazelcastPukoMap")
@AllArgsConstructor
@NoArgsConstructor
public class HazelcastPuko implements Puko, DataSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8861168166855985284L;

	@Id
	private String key;
	
	private String value;
	
	private Date timestamp;
/*
	@Override
	public int getFactoryId() {
		return 1;
	}

	@Override
	public int getId() {
		return 1;
	}

*/
	//This method is called under the hoods by Java to write data to the database
	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(key);
		out.writeUTF(value);
		out.writeLong(timestamp.getTime());
	}

    //This method is called under the hoods by Java to read data from the database
	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.key = in.readUTF();
		this.value = in.readUTF();
		this.timestamp = new Date(in.readLong());
	}

}
