package com.team2813;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import com.team2813.RobotSpecificConfigs.SwerveConfig;

public class RobotSpecificConfigsTest {
	@Test
	public void serializationTest() throws IOException, ClassNotFoundException {
		SwerveConfig cnf = new SwerveConfig();
		byte[] serialized = null;
		SwerveConfig deserialized = null;
		try (
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ObjectOutputStream objStream = new ObjectOutputStream(stream)
		) {
			objStream.writeObject(cnf);
			serialized = stream.toByteArray();
		}
		assertTrue("Failed serialization", serialized != null);
		try (
			ByteArrayInputStream stream = new ByteArrayInputStream(serialized);
			ObjectInputStream objStream = new ObjectInputStream(stream)
		) {
			Object obj = objStream.readObject();
			assertTrue("did not deserialize to SwerveConfig", obj instanceof SwerveConfig);
			deserialized = (SwerveConfig) obj;
		}
		assertTrue("Failed deserialization", deserialized != null);
		assertEquals("Did not produce identical object", cnf, deserialized);
	}
}
